package nineto6.Talk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
import nineto6.Talk.controller.swagger.AuthControllerDocs;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {
    private final RefreshRedisRepository refreshTokenRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;

    /**
     *  Refresh-Token 으로 부터 토큰 재발급
     */
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
            ResponseCookie responseCookie = ResponseCookie.from(AuthConstants.REFRESH_TOKEN, newRefreshToken)
                    .maxAge(3 * 24 * 60 * 60)
                    .path("/api/auth")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            response.addHeader(AuthConstants.SET_COOKIE, responseCookie.toString());

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
            redisTemplate.opsForValue().set(refToken, "true", expiration, TimeUnit.MILLISECONDS);

            // 9. 데이터 세팅
            HashMap<String, Object> accessToken = new HashMap<>();
            accessToken.put(AuthConstants.ACCESS_TOKEN, newAccessToken);

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
            redisTemplate.opsForValue().set(refToken, "true", expiration, TimeUnit.MILLISECONDS);

            // Set-Cookie 빈 값에 만료기간 0초로 변경하기
            ResponseCookie cookie = ResponseCookie.from(AuthConstants.REFRESH_TOKEN, "")
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            response.addHeader(AuthConstants.SET_COOKIE, cookie.toString());
        }

        throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
    }

    /**
     * Access-Token 으로부터 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = AuthConstants.AUTH_HEADER) String authorization,
                                              @AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                              HttpServletResponse response) {
        // Header 에서 JWT Access Token 추출
        String token = TokenUtils.getAccessTokenFormHeader(authorization);

        // Redis 에서 해당  member Email 로 저장된 Refresh Token 이 있는지 여부를 확인 (없으면 예외)
        RefreshToken refreshTokenObject = refreshTokenRedisRepository.findByMemberEmail(memberDetailsDto.getMemberDto().getMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 재발급을 불가능하게 만든다 (redis 내에 삭제)
        refreshTokenRedisRepository.delete(refreshTokenObject);

        // 해당 Access Token 유효시간을 가지고 와서 블랙 리스트에 등록하기
        Long expiration = TokenUtils.getExpirationFormAccessToken(token);
        redisTemplate.opsForValue().set(token, "true", expiration, TimeUnit.MILLISECONDS);

        // Set-Cookie 빈 값에 만료기간 0초로 변경하기
        ResponseCookie cookie = ResponseCookie.from(AuthConstants.REFRESH_TOKEN, "")
                .maxAge(0)
                .path("/api/auth")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader(AuthConstants.SET_COOKIE, cookie.toString());

        log.info("[MemberController] 로그아웃 성공");
        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.LOGOUT_SUCCESS.getStatus())
                .message(SuccessCode.LOGOUT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}
