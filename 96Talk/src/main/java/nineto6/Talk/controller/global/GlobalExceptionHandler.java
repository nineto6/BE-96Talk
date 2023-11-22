package nineto6.Talk.controller.global;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.common.exception.ResourceExceptionHandler;
import nineto6.Talk.model.response.ApiResponse;
import org.springframework.core.io.Resource;
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
        ApiResponse ar = ApiResponse.builder()
                .result(null)
                .status(ex.getErrorCode().getStatus())
                .message(ex.getErrorCode().getMessage())
                .build();

        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @ExceptionHandler(ResourceExceptionHandler.class)
    public ResponseEntity<Resource> ResourceBusinessExHandler(ResourceExceptionHandler ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
