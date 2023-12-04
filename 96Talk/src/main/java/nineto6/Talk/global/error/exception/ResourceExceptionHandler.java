package nineto6.Talk.global.error.exception;

import lombok.Builder;
import lombok.Getter;
import nineto6.Talk.global.error.exception.code.ErrorCode;

/**
 * 리소스 전용 Exception
 */
public class ResourceExceptionHandler extends RuntimeException{
    @Getter
    private final ErrorCode errorCode;

    @Builder
    public ResourceExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public ResourceExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
