package nineto6.Talk.global.error;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.ResourceExceptionHandler;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * BusinessException 에서 발생한 에러
     */
    @ExceptionHandler(BusinessExceptionHandler.class)
    public ResponseEntity<ApiResponse> businessExHandler(BusinessExceptionHandler ex) {
        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(ex.getErrorCode().getStatus())
                .message(ex.getErrorCode().getMessage())
                .build();

        return new ResponseEntity<>(ar, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ResourceExceptionHandler.class)
    public ResponseEntity<Resource> resourceBusinessExHandler(ResourceExceptionHandler ex) {
        return new ResponseEntity<>(ex.getErrorCode().getHttpStatus());
    }
}
