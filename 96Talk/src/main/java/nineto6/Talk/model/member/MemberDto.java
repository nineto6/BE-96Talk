package nineto6.Talk.model.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MemberDto {
    private Long memberId;
    private String memberEmail;
    private String memberPwd;
    private String memberNickname;
    private LocalDateTime memberRegdate;
    private List<String> roleList;
    @Builder
    public MemberDto(Long memberId, String memberEmail, String memberPwd, String memberNickname, LocalDateTime memberRegdate, List<String> roleList) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNickname = memberNickname;
        this.memberRegdate = memberRegdate;
        this.roleList = roleList;
    }
}
