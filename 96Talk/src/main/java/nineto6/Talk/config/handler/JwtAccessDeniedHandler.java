package nineto6.Talk.config.handler;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
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
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        JSONObject jsonObject = jsonResponseWrapper(accessDeniedException);

        printWriter.print(jsonObject);
        printWriter.close();
    }

    private JSONObject jsonResponseWrapper (Exception e) {
        log.error("403 Forbidden 에러");

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("result", ErrorCode.FORBIDDEN_ERROR.getCode());
        jsonMap.put("status", ErrorCode.FORBIDDEN_ERROR.getStatus());
        jsonMap.put("message", ErrorCode.FORBIDDEN_ERROR.getMessage());
        return new JSONObject(jsonMap);
    }
}
