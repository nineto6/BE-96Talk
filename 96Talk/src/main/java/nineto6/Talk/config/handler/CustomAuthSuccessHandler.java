package nineto6.Talk.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.AuthConstants;
import nineto6.Talk.common.utils.NetUtils;
import nineto6.Talk.common.utils.TokenUtils;
import nineto6.Talk.config.JwtToken;
import nineto6.Talk.config.refresh.RefreshRedisRepository;
import nineto6.Talk.config.refresh.RefreshToken;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberDto;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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
        log.debug("3. CustomLoginSuccessHandler");

        // [STEP1] 사용자와 관련된 정보를 모두 조회합니다.
        MemberDto memberDto = ((MemberDetailsDto) authentication.getPrincipal()).getMemberDto();

        // [STEP2] 조회한 데이터를 JSONObject 형태로 파싱을 수행합니다.
        JSONObject memberVoObj = (JSONObject) JSONValue
                .parse(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(memberDto));

        // Refresh-Token 쿠키 저장 (httpOnly)
        JwtToken jwtToken = TokenUtils.generateJwtToken(memberDto);
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                .maxAge(3 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());

        // refreshToken 이 유효 하지만 사용자가 다시 로그인을 진행한 경우
        RefreshToken refreshToken = refreshRedisRepository.findByMemberEmail(memberDto.getMemberEmail());
        if(!ObjectUtils.isEmpty(refreshToken)) {
            // Redis Refresh-Token id 로 정보 업데이트 (기존 정보 덮어쓰기)
            refreshRedisRepository.save(RefreshToken.builder()
                    .id(refreshToken.getId())
                    .ip(refreshToken.getIp())
                    .memberEmail(refreshToken.getMemberEmail())
                    .refreshToken(jwtToken.getRefreshToken())
                    .accessToken(jwtToken.getAccessToken())
                    .build());
        } else {
            // 아닐경우 Redis Refresh-Token 정보 새로 저장
            refreshRedisRepository.save(RefreshToken.builder()
                    .id(null)
                    .ip(NetUtils.getClientIp(request))
                    .memberEmail(memberDto.getMemberEmail())
                    .refreshToken(jwtToken.getRefreshToken())
                    .accessToken(jwtToken.getAccessToken())
                    .build());
        }

        // 1. 데이터 세팅
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> accessToken = new HashMap<>();
        JSONObject jsonObject;

        accessToken.put("accessToken", jwtToken.getAccessToken());
        responseMap.put("result", accessToken);
        responseMap.put("status", 200);
        responseMap.put("message", "login success");
        jsonObject = new JSONObject(responseMap);

        log.debug("[로그인 성공]현재 요청된 IP : {}", NetUtils.getClientIp(request)); // 클라이언트 IP 확인 로그

        // [STEP4] 구성한 응답 값을 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject); // 최종 저장된 '사용자 정보', '사이트 정보' Front 전달
        printWriter.flush();
        printWriter.close();
    }
}
