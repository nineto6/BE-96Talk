package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {
    private Long memberId;
    private String memberEmail;
    private String memberPwd;
    private String memberNm;
    private LocalDateTime memberRegdate;
    private List<String> roleList;
    @Builder
    public MemberAuthority(Long memberId, String memberEmail, String memberPwd, String memberNm, LocalDateTime memberRegdate, List<String> roleList) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNm = memberNm;
        this.memberRegdate = memberRegdate;
        this.roleList = roleList;
    }
}
