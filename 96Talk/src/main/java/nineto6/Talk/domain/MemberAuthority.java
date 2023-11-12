package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nineto6.Talk.model.member.MemberDto;

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

    public MemberDto toDto() {
        return MemberDto.builder()
                .memberId(this.memberId)
                .memberEmail(this.memberEmail)
                .memberPwd(this.memberPwd)
                .memberNm(this.memberNm)
                .memberRegdate(this.memberRegdate)
                .roleList(this.roleList)
                .build();
    }
}
