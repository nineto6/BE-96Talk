package nineto6.Talk.config.handler;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 사용자의 ‘인증’에 대해 실패하였을 경우 수행되는 Handler 로 실패에 대한 사용자에게 반환값을 구성하여 전달합니다.
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // [STEP1] 클라이언트로 전달 할 응답 값을 구성합니다.
        JSONObject jsonObject = new JSONObject();
        String failMsg = "";

        // [STEP2] 응답 값을 구성하고 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        log.debug("failMsg: {}", failMsg);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", ErrorCode.BADREQUEST_ERROR.getCode());
        resultMap.put("status", ErrorCode.BADREQUEST_ERROR.getStatus());
        resultMap.put("message", ErrorCode.BADREQUEST_ERROR.getMessage());

        jsonObject = new JSONObject(resultMap);
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
