package nineto6.Talk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.AuthConstants;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.common.utils.RefreshTokenUtils;
import nineto6.Talk.common.utils.TokenUtils;
import nineto6.Talk.config.refresh.RefreshRedisRepository;
import nineto6.Talk.config.refresh.RefreshToken;
import nineto6.Talk.controller.swagger.AuthControllerDocs;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.model.member.MemberLoginRequest;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {
    private final RefreshRedisRepository refreshTokenRedisRepository;
    private final MemberService memberService;

    @Override
    @PostMapping("/login")
    public void login(@RequestBody MemberLoginRequest memberLoginRequest) {}

    /**
     *  Refresh-Token 으로 부터 토큰 재발급
     */
    @Override
    @PutMapping()
    public ResponseEntity<ApiResponse> reissue(@RequestHeader(value="Cookie") String refreshToken,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        String refToken = RefreshTokenUtils.removePrefix(refreshToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 저장된 refresh token 찾기. 없으면 예외
        RefreshToken refreshTokenObject = refreshTokenRedisRepository.findByRefreshToken(refToken)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

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
                .memberEmail(memberDto.getMemberEmail())
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .build());

        // 9. 데이터 세팅
        HashMap<String, Object> accessToken = new HashMap<>();
        accessToken.put(AuthConstants.ACCESS_TOKEN, newAccessToken);

        // 10. 응답
        ApiResponse ar = ApiResponse.builder()
                .result(accessToken)
                .status(SuccessCode.REISSUE_SUCCESS.getStatus())
                .message(SuccessCode.REISSUE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(ar, SuccessCode.REISSUE_SUCCESS.getHttpStatus());
    }

    /**
     * Access-Token 으로부터 로그아웃
     */
    @Override
    @DeleteMapping()
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
        return new ResponseEntity<>(ar, SuccessCode.LOGOUT_SUCCESS.getHttpStatus());
    }
}
