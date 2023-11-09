package nineto6.Talk.controller.global;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
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
        log.error("[exceptionHandler] ex", ex);

        ApiResponse ar = ApiResponse.builder()
                .result(ex.getErrorCode().getCode())
                .status(ex.getErrorCode().getStatus())
                .message(ex.getErrorCode().getMessage())
                .build();

        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}
