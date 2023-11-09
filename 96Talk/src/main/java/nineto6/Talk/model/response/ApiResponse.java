package nineto6.Talk.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse {
    @Schema(title = "결과 또는 비즈니스 에러 코드")
    Object result;
    @Schema(title = "Http 상태코드")
    int status;
    @Schema(title = "메세지")
    String message;
    @Builder
    public ApiResponse(Object result, int status, String message) {
        this.result = result;
        this.status = status;
        this.message = message;
    }
}
