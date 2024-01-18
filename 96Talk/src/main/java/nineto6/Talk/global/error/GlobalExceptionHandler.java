package nineto6.Talk.global.error;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.ResourceExceptionHandler;
import nineto6.Talk.global.common.response.ApiResponse;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Business Exception Handler
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

    /**
     * Resource Exception Handler
     */
    @ExceptionHandler(ResourceExceptionHandler.class)
    public ResponseEntity<Resource> resourceBusinessExHandler(ResourceExceptionHandler ex) {
        return new ResponseEntity<>(ex.getErrorCode().getHttpStatus());
    }

    /**
     * Validation Exception Handler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(ErrorCode.VALIDATION_EXCEPTION_ERROR.getStatus())
                .message(ErrorCode.VALIDATION_EXCEPTION_ERROR.getMessage())
                .build();

        return new ResponseEntity<>(ar, ErrorCode.VALIDATION_EXCEPTION_ERROR.getHttpStatus());
    }
}
