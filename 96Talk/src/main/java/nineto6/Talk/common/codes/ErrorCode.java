package nineto6.Talk.common.codes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리한다.
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {
    BAD_REQUEST_ERROR(400, "BAD REQUEST ERROR"),
    UNAUTHORIZED_ERROR(401, "UNAUTHORIZED ERROR"),
    FORBIDDEN_ERROR(403, "FORBIDDEN ERROR"),

    /**
     * *********************************** custom Error CodeList ********************************************
     */
    
    INSERT_ERROR(400, "INSERT TRANSACTION ERROR EXCEPTION"),
    UPDATE_ERROR(400, "UPDATE TRANSACTION ERROR EXCEPTION"),
    DELETE_ERROR(400, "DELETE TRANSACTION ERROR EXCEPTION"),

    BUSINESS_EXCEPTION_ERROR(400, "BUSINESS EXCEPTION ERROR"),
    DUPLICATE_ERROR(400, "DUPLICATE ERROR EXCEPTION"),

    ; // End

    /**
     * *********************************** Error Code Constructor ********************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private int status;
    // 에러코드의 '코드 메시지'을 반환한다.
    private String message;

    // 생성자 구성
    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
