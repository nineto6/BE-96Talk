package nineto6.Talk.common.exception;

import lombok.Builder;
import lombok.Getter;
import nineto6.Talk.common.codes.ErrorCode;

/**
 * 에러를 사용하기 위한 구현체
 */
public class BusinessExceptionHandler extends RuntimeException{
    @Getter
    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
