package nineto6.Talk.controller.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@Schema(title = "사용자 정보 등록")
public class MemberSaveRequest {
    @Schema(title = "사용자 이메일")
    @NotNull()
    @Email()
    private String memberEmail;
    @Schema(title = "사용자 비밀번호")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
    private String memberPwd;
    @Schema(title = "사용자 닉네임")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$")
    private String memberNickname;
    @Builder
    public MemberSaveRequest(String memberEmail, String memberPwd, String memberNickname) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNickname = memberNickname;
    }
}
