package nineto6.Talk.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 401 Unauthorized Exception 처리를 위한 클래스
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("[JwtAuthenticationEntryPoint] 401 Unauthorized 에러");

        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(ErrorCode.UNAUTHORIZED_ERROR.getStatus())
                .message(ErrorCode.UNAUTHORIZED_ERROR.getMessage())
                .build();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(ErrorCode.UNAUTHORIZED_ERROR.getHttpStatus().value());

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        printWriter.print(objectMapper.writeValueAsString(ar));
        printWriter.close();
    }
}
