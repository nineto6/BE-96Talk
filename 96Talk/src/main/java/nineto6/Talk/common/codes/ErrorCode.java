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
    BUSINESS_EXCEPTION_ERROR(400, "AAAA", "Business Exception Error"),
    UNAUTHORIZED_ERROR(401, "BBBB", "Unauthorized Error"),
    FORBIDDEN_ERROR(403, "CCCC", "Forbidden Error"),
    BADREQUEST_ERROR(400, "DDDD", "BadRequest Error"),

    /**
     * *********************************** custom Error CodeList ********************************************
     */
    
    INSERT_ERROR(400, "0001", "Insert Transaction Error Exception"),
    UPDATE_ERROR(400, "0002", "Update Transaction Error Exception"),
    DELETE_ERROR(400, "0003", "Delete Transaction Error Exception"),
    DUPLICATE_ERROR(400, "0004", "Duplicate Error Exception"),

    ; // End

    /**
     * *********************************** Error Code Constructor ********************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private String code;

    // 에러코드의 '코드 메시지'을 반환한다.
    private String message;

    // 생성자 구성
    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
