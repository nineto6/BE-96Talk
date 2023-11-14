package nineto6.Talk.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.AuthConstants;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.common.utils.NetUtils;
import nineto6.Talk.common.utils.TokenUtils;
import nineto6.Talk.config.JwtToken;
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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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

        if(refreshToken.length() < "refreshToken=".length()) {
            throw new BusinessExceptionHandler(ErrorCode.BADREQUEST_ERROR);
        }

        String token = refreshToken.substring("refreshToken=".length()); // 앞 부분 "refreshToken=" 제거

        // 2. validateToken 메서드로 refreshToken 유효성 검사
        if (TokenUtils.isValidRefreshToken(token)) {
            // 3. 저장된 refresh token 찾기 (로그아웃 되어 있으면 재발급 안됨)
            RefreshToken byRefreshToken = refreshTokenRedisRepository.findByRefreshToken(token);

            if (!ObjectUtils.isEmpty(byRefreshToken)) {
                // 4. 최초 로그인한 ip 값 가져오기
                String currentIpAddress = NetUtils.getClientIp(request);

                // 5. 최초 로그인한 IP 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                if (byRefreshToken.getIp().equals(currentIpAddress)) {
                    MemberDto memberDto;
                    try {
                        // 6. findByEmail 실행 후 userDto 값 가져오기
                        memberDto = memberService.login(byRefreshToken.getMemberEmail())
                                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BADREQUEST_ERROR)); // 값이 없을 경우 잘못 된 요청
                    } catch(AuthenticationServiceException e) {
                        throw new BusinessExceptionHandler(ErrorCode.BADREQUEST_ERROR);
                    }

                    if(ObjectUtils.isEmpty(memberDto)) {
                        throw new BusinessExceptionHandler(ErrorCode.BADREQUEST_ERROR);
                    }

                    // 7. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                    JwtToken jwtToken = TokenUtils.generateJwtToken(memberDto);
                    ResponseCookie responseCookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                            .maxAge(3 * 24 * 60 * 60)
                            .path("/")
                            .secure(true)
                            .sameSite("None")
                            .httpOnly(true)
                            .build();
                    response.addHeader("Set-Cookie", responseCookie.toString());

                    // 8. Redis Refresh-Token id 로 정보 업데이트 (기존 정보 덮어쓰기)
                    refreshTokenRedisRepository.save(RefreshToken.builder()
                            .id(byRefreshToken.getId())
                            .ip(currentIpAddress)
                            .memberEmail(memberDto.getMemberEmail())
                            .refreshToken(jwtToken.getRefreshToken())
                            .accessToken(jwtToken.getAccessToken())
                            .build());

                    // 문제 발생
                    // 재발급시에 전에 쓰였던 accessToken 이 만료기간이 지나지 않았을 경우에 인증 인가가 성공하게된다.
                    // 해결 방안 : 재발급시에 Access-Token 을 블랙리스트에 저장함으로 서
                    // JwtAuthorizationFilter 에서 인증을 실패하게 유도한다.

                    // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 저장하기
                    Long expiration = TokenUtils.getExpirationFormAccessToken(byRefreshToken.getAccessToken());
                    redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);

                    // 9. 데이터 세팅
                    HashMap<String, Object> accessToken = new HashMap<>();
                    accessToken.put("accessToken", jwtToken.getAccessToken());

                    // 10. 응답
                    log.info("재발급 성공");
                    ApiResponse ar = ApiResponse.builder()
                            .result(accessToken)
                            .status(SuccessCode.UPDATE_SUCCESS.getStatus())
                            .message(SuccessCode.UPDATE_SUCCESS.getMessage())
                            .build();

                    return new ResponseEntity<>(ar, HttpStatus.OK);
                } else { // 최초 로그인한 IP 와 다를 경우
                    log.info("[토큰 재발급] 최초 로그인한 IP 와 다릅니다.");
                }
            } else {
                log.info("[토큰 재발급] 현재 토큰은 로그아웃 되어 있습니다.)");
            }
        } else {
            log.info("[토큰 재발급] 유효하지 않은 Refresh-Token 입니다.");
        }

        ApiResponse ar = ApiResponse.builder()
                .result("It cannot be reissued") // 재발급 불가
                .status(ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus())
                .message(ErrorCode.BUSINESS_EXCEPTION_ERROR.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    /**
     * Access-Token 으로부터 로그아웃 (블랙리스트 저장)
     */
    @Operation(summary = "로그아웃", description = "헤더에 Access-Token 토큰이 필요합니다.")
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = AuthConstants.AUTH_HEADER) String authorization,
                                              @RequestHeader(value="Cookie") String refreshToken) {
        log.info("refreshToken : {}", refreshToken);
        // 1. Header 에서 JWT Access Token 추출
        String token = TokenUtils.getTokenFormHeader(authorization);

        // Access Token 에서 member Email 값을 가져온다
        String memberEmail = TokenUtils.getMemberEmailFormAccessToken(token);

        // Redis 에서 해당  member Email 로 저장된 Refresh Token 이 있는지 여부를 확인 후에 있을 경우 삭제를 한다.
        RefreshToken refreshTokenObject = refreshTokenRedisRepository.findByMemberEmail(memberEmail);
        if (!ObjectUtils.isEmpty(refreshTokenObject)) {
            // 재발급을 불가능하게 만든다 (redis 내에 삭제)
            refreshTokenRedisRepository.delete(refreshTokenObject);

            // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 저장하기
            Long expiration = TokenUtils.getExpirationFormAccessToken(token);
            redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);

            //TODO: Set-Cookie 만료기간 0초로 변경하기

            log.info("[로그아웃] 로그아웃 성공(블랙리스트 등록)");
            ApiResponse ar = ApiResponse.builder()
                    .result("Logout Success") // 이미 요청된 로그아웃
                    .status(SuccessCode.UPDATE_SUCCESS.getStatus())
                    .message(SuccessCode.UPDATE_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, HttpStatus.OK);
        }

        ApiResponse ar = ApiResponse.builder()
                .result("It cannot be logout") // 이미 요청된 로그아웃
                .status(ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus())
                .message(ErrorCode.BUSINESS_EXCEPTION_ERROR.getMessage())
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

        ApiResponse ar = ApiResponse.builder()
                .result(ErrorCode.DUPLICATE_ERROR.getCode())
                .status(ErrorCode.DUPLICATE_ERROR.getStatus())
                .message(ErrorCode.DUPLICATE_ERROR.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

}