package nineto6.Talk.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 사용자의 ‘인증’에 대해 실패하였을 경우 수행되는 Handler 로 실패에 대한 사용자에게 반환값을 구성하여 전달합니다.
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("[CustomAuthFailureHandler]");

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(ErrorCode.BAD_REQUEST_ERROR.getStatus())
                .message(ErrorCode.BAD_REQUEST_ERROR.getMessage())
                .build();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(ErrorCode.BAD_REQUEST_ERROR.getHttpStatus().value());

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        printWriter.print(objectMapper.writeValueAsString(apiResponse));
        printWriter.flush();
        printWriter.close();
    }
}
