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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody MemberSaveRequest memberSaveRequest) {
        memberService.signUp(memberSaveRequest);

        ApiResponse success = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.INSERT_SUCCESS.getStatus())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(success, SuccessCode.INSERT_SUCCESS.getHttpStatus());
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse> duplicateCheckEmail(@RequestParam String memberEmail) {
        boolean isEmpty = memberService.duplicateCheckEmail(memberEmail);

        if(isEmpty) {
            ApiResponse ar = ApiResponse.builder()
                    .result(null)
                    .status(SuccessCode.ALLOW_EMAIL.getStatus())
                    .message(SuccessCode.ALLOW_EMAIL.getMessage())
                    .build();
            return new ResponseEntity<>(ar, SuccessCode.ALLOW_EMAIL.getHttpStatus());
        }

        throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse> duplicateCheckNickname(@RequestParam String memberNickname) {
        boolean isEmpty = memberService.duplicateCheckNickname(memberNickname);

        if(isEmpty) {
            ApiResponse ar = ApiResponse.builder()
                    .result(null)
                    .status(SuccessCode.ALLOW_NICKNAME.getStatus())
                    .message(SuccessCode.ALLOW_NICKNAME.getMessage())
                    .build();
            return new ResponseEntity<>(ar, SuccessCode.ALLOW_NICKNAME.getHttpStatus());
        }

        throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
    }

}