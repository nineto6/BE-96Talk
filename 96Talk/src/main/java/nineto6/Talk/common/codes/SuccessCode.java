package nineto6.Talk.common.codes;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Success CodeList : 성공 코드를 관리한다.
 * Success Code Constructor : 성공 코드를 사용하기 위한 생성자를 구성한다.
 *
 */
@Getter
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // 조회 성공 코드 (HTTP Response: 200 OK)
    SELECT_SUCCESS(200, HttpStatus.OK, "SELECT SUCCESS"),
    // 삭제 성공 코드 (HTTP Response: 200 OK)
    DELETE_SUCCESS(200, HttpStatus.OK,"DELETE SUCCESS"),
    // 삽입 성공 코드 (HTTP Response: 201 Created)
    INSERT_SUCCESS(201, HttpStatus.CREATED, "INSERT SUCCESS"),
    // 수정 성공 코드 (HTTP Response: 201 Created)
    UPDATE_SUCCESS(204, HttpStatus.NO_CONTENT, "UPDATE SUCCESS"),

    REISSUE_SUCCESS(200, HttpStatus.OK,"REISSUE SUCCESS"),
    LOGOUT_SUCCESS(200, HttpStatus.OK,"LOGOUT SUCCESS"),
    LOGIN_SUCCESS(200, HttpStatus.OK,"LOGIN SUCCESS"),

    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // 성공 코드의 '코드 상태'를 반환한다.
    private final int status;
    // 성공 코드의 HTTP 상태 코드를 반환한다.
    private final HttpStatus httpStatus;
    // 성공 코드의 '코드 메시지'를 반환한다.s
    private final String message;

    // 생성자 구성
    SuccessCode(final int status, final HttpStatus httpStatus, final String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}