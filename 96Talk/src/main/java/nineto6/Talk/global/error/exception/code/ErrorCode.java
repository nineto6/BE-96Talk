package nineto6.Talk.global.error.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리한다.
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {
    BAD_REQUEST_ERROR(-1, HttpStatus.BAD_REQUEST, "BAD REQUEST ERROR"),
    UNAUTHORIZED_ERROR(-2, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED ERROR"),
    FORBIDDEN_ERROR(-3, HttpStatus.FORBIDDEN, "FORBIDDEN ERROR"),

    /**
     * *********************************** custom Error CodeList ********************************************
     */
    
    INSERT_ERROR(-4, HttpStatus.BAD_REQUEST, "INSERT TRANSACTION ERROR EXCEPTION"),
    UPDATE_ERROR(-5, HttpStatus.BAD_REQUEST, "UPDATE TRANSACTION ERROR EXCEPTION"),
    DELETE_ERROR(-6, HttpStatus.BAD_REQUEST, "DELETE TRANSACTION ERROR EXCEPTION"),

    BUSINESS_EXCEPTION_ERROR(-7, HttpStatus.BAD_REQUEST, "BUSINESS EXCEPTION ERROR"),
    DUPLICATE_ERROR(-8, HttpStatus.BAD_REQUEST, "DUPLICATE ERROR EXCEPTION"),
    FILE_EXCEPTION_ERROR(-9, HttpStatus.BAD_REQUEST, "FILE EXCEPTION ERROR"),
    RESOURCE_EXCEPTION_ERROR(-10, HttpStatus.BAD_REQUEST, "RESOURCE DOWNLOAD EXCEPTION ERROR"),
    MESSAGE_EXCEPTION_ERROR(-11, HttpStatus.BAD_REQUEST, "MESSAGE EXCEPTION ERROR"),
    ; // End

    /**
     * *********************************** Error Code Constructor ********************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private int status;
    private HttpStatus httpStatus;
    // 에러코드의 '코드 메시지'을 반환한다.
    private String message;

    // 생성자 구성
    ErrorCode(final int status, final HttpStatus httpStatus, final String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
