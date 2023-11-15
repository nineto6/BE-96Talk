package nineto6.Talk.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.utils.NetUtils;
import nineto6.Talk.common.utils.TokenUtils;
import nineto6.Talk.config.refresh.RefreshRedisRepository;
import nineto6.Talk.config.refresh.RefreshToken;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberDto;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자의 ‘인증’에 대해 성공하였을 경우 수행되는 Handler 로 성공에 대한 사용자에게 반환값을 구성하여 전달합니다
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RefreshRedisRepository refreshRedisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // 사용자와 관련된 정보를 모두 조회합니다.
        MemberDto memberDto = ((MemberDetailsDto) authentication.getPrincipal()).getMemberDto();

        // AccessToken, Refresh-Token 생성
        String accessToken = TokenUtils.generateJwtToken(memberDto);
        String refreshToken = UUID.randomUUID().toString();

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(3 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());

        // refreshToken 이 유효 하지만 사용자가 다시 로그인을 진행한 경우
        RefreshToken refreshTokenObject = refreshRedisRepository.findByMemberEmail(memberDto.getMemberEmail())
                .orElse(null);

        if(!ObjectUtils.isEmpty(refreshTokenObject)) {
            // Redis id 로 정보 업데이트 (덮어쓰기)
            refreshRedisRepository.save(RefreshToken.builder()
                    .id(refreshTokenObject.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .ip(NetUtils.getClientIp(request))
                    .memberEmail(refreshTokenObject.getMemberEmail())
                    .build());
            log.info("[CustomAuthSuccessHandler] RefreshToken 업데이트");
        } else {
            // 아닐경우 Redis Refresh-Token 정보 새로 저장
            refreshRedisRepository.save(RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .ip(NetUtils.getClientIp(request))
                    .memberEmail(memberDto.getMemberEmail())
                    .build());
            log.info("[CustomAuthSuccessHandler] RefreshToken 생성");
        }

        // 1. 데이터 세팅
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> accessTokenMap = new HashMap<>();
        accessTokenMap.put("accessToken", accessToken);

        responseMap.put("result", accessTokenMap);
        responseMap.put("status", 200);
        responseMap.put("message", "login success");

        JSONObject jsonObject = new JSONObject(responseMap);

        // [STEP4] 구성한 응답 값을 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject); // 최종 저장된 '사용자 정보', '사이트 정보' Front 전달
        printWriter.flush();
        printWriter.close();
    }
}
