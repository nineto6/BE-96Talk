package nineto6.Talk.model.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberLoginRequest {
    private String memberEmail;
    private String memberPwd;
    @Builder
    public MemberLoginRequest(String memberEmail, String memberPwd) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
    }
}
