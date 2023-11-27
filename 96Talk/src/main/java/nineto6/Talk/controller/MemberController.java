package nineto6.Talk.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.controller.swagger.MemberControllerDocs;
import nineto6.Talk.model.member.MemberSaveRequest;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberControllerDocs {
    private final MemberService memberService;
    /**
     * 회원가입
     */
    @PostMapping()
    public ResponseEntity<ApiResponse> signUp(@RequestBody MemberSaveRequest memberSaveRequest) {
        memberService.signUp(memberSaveRequest);

        ApiResponse success = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.INSERT_SUCCESS.getStatus())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(success, SuccessCode.INSERT_SUCCESS.getHttpStatus());
    }
    /**
     * 이메일 및 닉네임 중복 체크
     */
    @GetMapping("/duplicate")
    public ResponseEntity<ApiResponse> duplicateCheck(@RequestParam(value = "memberEmail", required = false) String memberEmail,
                                                      @RequestParam(value = "memberNm", required = false) String memberNm) {
        if(!ObjectUtils.isEmpty(memberEmail)) {
            boolean isEmpty = memberService.duplicateCheckEmail(memberEmail);
            ApiResponse ar = ApiResponse.builder()
                    .result(isEmpty) // true , false
                    .status(SuccessCode.SELECT_SUCCESS.getStatus())
                    .message(SuccessCode.SELECT_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, SuccessCode.SELECT_SUCCESS.getHttpStatus());
        }

        if(!ObjectUtils.isEmpty(memberNm)) {
            boolean isEmpty = memberService.duplicateCheckNickname(memberNm);
            ApiResponse ar = ApiResponse.builder()
                    .result(isEmpty) // true , false
                    .status(SuccessCode.SELECT_SUCCESS.getStatus())
                    .message(SuccessCode.SELECT_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, SuccessCode.SELECT_SUCCESS.getHttpStatus());
        }

        throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
    }

}