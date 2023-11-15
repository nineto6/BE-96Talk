package nineto6.Talk.model.member;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(title = "사용자 로그인")
public class MemberLoginRequest {
    @Schema(title = "사용자 이메일")
    private String memberEmail;
    @Schema(title = "사용자 비밀번호")
    private String memberPwd;
    @Builder
    public MemberLoginRequest(String memberEmail, String memberPwd) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
    }
}
