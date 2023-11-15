package nineto6.Talk.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.AuthConstants;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.common.utils.NetUtils;
import nineto6.Talk.common.utils.RefreshTokenUtils;
import nineto6.Talk.common.utils.TokenUtils;
import nineto6.Talk.config.refresh.RefreshRedisRepository;
import nineto6.Talk.config.refresh.RefreshToken;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.model.member.MemberSaveRequest;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
    private final RefreshRedisRepository refreshTokenRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;

    /**
     * 회원가입
     */
    @Operation(summary = "회원가입", description = "회원가입용 메서드입니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody MemberSaveRequest memberSaveRequest) {

        memberService.signUp(memberSaveRequest);

        ApiResponse success = ApiResponse.builder()
                .result(SuccessCode.INSERT_SUCCESS.getCode())
                .status(SuccessCode.INSERT_SUCCESS.getStatus())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    /**
     *  Refresh-Token 으로 부터 재발급 (JwtAuthorizationFilter 인증 X)
     */
    @Operation(summary = "토큰 재발급", description = "토큰 재발급용 메서드입니다.")
    @GetMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(@RequestHeader(value="Cookie") String refreshToken,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        String refToken = RefreshTokenUtils.removePrefix(refreshToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 저장된 refresh token 찾기. 없으면 예외
        RefreshToken refreshTokenObject = refreshTokenRedisRepository.findByRefreshToken(refToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 현재 요청한 IP 값 가져오기
        String currentIpAddress = NetUtils.getClientIp(request);

        // 최초 로그인한 IP 와 같은지 확인
        if (refreshTokenObject.getIp().equals(currentIpAddress)) {
            // findByEmail 실행 후 memberDto 값 가져오기 (없으면 예외)
            MemberDto memberDto = memberService.login(refreshTokenObject.getMemberEmail())
                    .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

            // memberDto 정보를 기반으로 JWT Token 생성
            String newAccessToken = TokenUtils.generateJwtToken(memberDto);
            String newRefreshToken = UUID.randomUUID().toString();

            // Refresh Token 쿠키 생성
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .maxAge(3 * 24 * 60 * 60)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            response.addHeader("Set-Cookie", responseCookie.toString());

            // 조회한 id 로 Redis 정보 업데이트 (기존 정보 덮어쓰기)
            refreshTokenRedisRepository.save(RefreshToken.builder()
                    .id(refreshTokenObject.getId())
                    .ip(currentIpAddress)
                    .memberEmail(memberDto.getMemberEmail())
                    .refreshToken(newRefreshToken)
                    .accessToken(newAccessToken)
                    .build());

            // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 저장하기
            Long expiration = TokenUtils.getExpirationFormAccessToken(refreshTokenObject.getAccessToken());
            redisTemplate.opsForValue().set(refToken, "logout", expiration, TimeUnit.MILLISECONDS);

            // 9. 데이터 세팅
            HashMap<String, Object> accessToken = new HashMap<>();
            accessToken.put("accessToken", newAccessToken);

            // 10. 응답
            ApiResponse ar = ApiResponse.builder()
                    .result(accessToken)
                    .status(SuccessCode.REISSUE_SUCCESS.getStatus())
                    .message(SuccessCode.REISSUE_SUCCESS.getMessage())
                    .build();

            return new ResponseEntity<>(ar, HttpStatus.OK);
        } else {
            log.info("[MemberController] 최초 로그인한 IP 와 다릅니다.");
            // * IP 값이 다를 때 메일 알림을 주는 방법이 있다. *
            // Refresh Token 제거
            refreshTokenRedisRepository.delete(refreshTokenObject);

            // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 저장하기
            Long expiration = TokenUtils.getExpirationFormAccessToken(refreshTokenObject.getAccessToken());
            redisTemplate.opsForValue().set(refToken, "logout", expiration, TimeUnit.MILLISECONDS);

            // Set-Cookie 빈 값에 만료기간 0초로 변경하기
            ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
        }

        throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
    }

    /**
     * Access-Token 으로부터 로그아웃 (블랙리스트 저장)
     */
    @Operation(summary = "로그아웃", description = "헤더에 Access-Token 토큰이 필요합니다.")
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = AuthConstants.AUTH_HEADER) String authorization,
                                              @RequestHeader(value="Cookie") String refreshToken,
                                              HttpServletResponse response) {
        // Header 에서 JWT Access Token 추출
        String token = TokenUtils.getAccessTokenFormHeader(authorization);

        // Cookie 에서 JWT Refresh Token 추출 (없으면 예외)
        String refToken = RefreshTokenUtils.removePrefix(refreshToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // Redis 에서 해당  member Email 로 저장된 Refresh Token 이 있는지 여부를 확인 (없으면 예외)
        RefreshToken refreshTokenObject = refreshTokenRedisRepository.findByRefreshToken(refToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 재발급을 불가능하게 만든다 (redis 내에 삭제)
        refreshTokenRedisRepository.delete(refreshTokenObject);

        // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 등록하기
        Long expiration = TokenUtils.getExpirationFormAccessToken(token);
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);

        // Set-Cookie 빈 값에 만료기간 0초로 변경하기
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        log.info("[MemberController] 로그아웃 성공");
        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.LOGOUT_SUCCESS.getStatus())
                .message(SuccessCode.LOGOUT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }


    /**
     * 이메일 및 닉네임 중복 체크
     */
    @Operation(summary = "중복 체크", description = "중복 체크용 메서드입니다.")
    @GetMapping("/duplicateCheck")
    public ResponseEntity<ApiResponse> duplicateCheck(@RequestParam(value = "memberEmail", required = false) String memberEmail,
                                                      @RequestParam(value = "memberNm", required = false) String memberNm) {
        if(!ObjectUtils.isEmpty(memberEmail)) {
            boolean isEmpty = memberService.duplicateCheckEmail(memberEmail);
            ApiResponse ar = ApiResponse.builder()
                    .result(isEmpty) // true , false
                    .status(SuccessCode.SELECT_SUCCESS.getStatus())
                    .message(SuccessCode.SELECT_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }

        if(!ObjectUtils.isEmpty(memberNm)) {
            boolean isEmpty = memberService.duplicateCheckNickname(memberNm);
            ApiResponse ar = ApiResponse.builder()
                    .result(isEmpty) // true , false
                    .status(SuccessCode.SELECT_SUCCESS.getStatus())
                    .message(SuccessCode.SELECT_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }

        throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
    }

}