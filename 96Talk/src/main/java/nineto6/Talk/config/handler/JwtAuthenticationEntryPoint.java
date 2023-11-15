package nineto6.Talk.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.model.response.ApiResponse;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.HashMap;

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
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.print(objectMapper.writeValueAsString(ar));
        printWriter.close();
    }
}
