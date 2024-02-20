package nineto6.Talk.domain.member.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.domain.member.controller.request.MemberSaveRequest;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "멤버")
public interface MemberControllerDocs {
    @Operation(summary = "회원가입", description = "생성 메서드, 회원가입용 메서드입니다.")
    ResponseEntity<ApiResponse> signUp(@RequestBody MemberSaveRequest memberSaveRequest);
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크용 메서드입니다.")
    ResponseEntity<ApiResponse> duplicateCheckEmail(String memberEmail);
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크용 메서드입니다.")
    ResponseEntity<ApiResponse> duplicateCheckNickname(String memberNickname);
}
