package nineto6.Talk.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.common.code.SuccessCode;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.domain.member.controller.swagger.MemberControllerDocs;
import nineto6.Talk.domain.member.dto.MemberSaveRequest;
import nineto6.Talk.global.common.response.ApiResponse;
import nineto6.Talk.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
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
                                                      @RequestParam(value = "memberNickname", required = false) String memberNickname) {
        if(!ObjectUtils.isEmpty(memberEmail)) {
            boolean isEmpty = memberService.duplicateCheckEmail(memberEmail);
            ApiResponse ar = ApiResponse.builder()
                    .result(isEmpty) // true , false
                    .status(SuccessCode.SELECT_SUCCESS.getStatus())
                    .message(SuccessCode.SELECT_SUCCESS.getMessage())
                    .build();
            return new ResponseEntity<>(ar, SuccessCode.SELECT_SUCCESS.getHttpStatus());
        }

        if(!ObjectUtils.isEmpty(memberNickname)) {
            boolean isEmpty = memberService.duplicateCheckNickname(memberNickname);
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