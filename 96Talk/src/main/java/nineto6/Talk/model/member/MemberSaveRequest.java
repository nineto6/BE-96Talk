package nineto6.Talk.model.member;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(title = "사용자 정보 등록")
public class MemberSaveRequest {
    @Schema(title = "사용자 이메일")
    private String memberEmail;
    @Schema(title = "사용자 비밀번호")
    private String memberPwd;
    @Schema(title = "사용자 닉네임")
    private String memberNm;
    @Builder
    public MemberSaveRequest(String memberEmail, String memberPwd, String memberNm) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNm = memberNm;
    }
}
