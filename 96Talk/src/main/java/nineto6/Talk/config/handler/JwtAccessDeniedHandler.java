package nineto6.Talk.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.model.response.ApiResponse;
import org.json.simple.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 403 Forbidden Exception 처리를 위한 클래스
 * 공통적인 응답을 위한 ErrorResponse
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[JwtAccessDeniedHandler] 403 Forbidden 에러");

        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(ErrorCode.FORBIDDEN_ERROR.getStatus())
                .message(ErrorCode.FORBIDDEN_ERROR.getMessage())
                .build();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.print(objectMapper.writeValueAsString(ar));
        printWriter.close();
    }
}
