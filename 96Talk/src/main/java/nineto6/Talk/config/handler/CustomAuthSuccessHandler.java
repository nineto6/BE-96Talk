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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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

        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject;

        // 1. 데이터 세팅
        responseMap.put("result", null);
        responseMap.put("status", 200);
        responseMap.put("message", "login success");
        jsonObject = new JSONObject(responseMap);

        // TODO: 추후 JWT 발급에 사용할 예정
        JwtToken jwtToken = TokenUtils.generateJwtToken(memberDto);
        response.addHeader(AuthConstants.AUTH_ACCESS, jwtToken.getAccessToken());
        response.addHeader(AuthConstants.AUTH_REFRESH, jwtToken.getRefreshToken());

        // Redis 정보 저장
        refreshRedisRepository.save(RefreshToken.builder()
                .id(null)
                .ip(NetUtils.getClientIp(request))
                .memberEmail(memberDto.getMemberEmail())
                .refreshToken(jwtToken.getRefreshToken())
                .build());

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
