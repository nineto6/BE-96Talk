package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private Long memberId;
    private String memberEmail;
    private String memberPwd;
    private String memberNm;
    private LocalDateTime memberRegdate;
    @Builder
    public Member(Long memberId, String memberEmail, String memberPwd, String memberNm, LocalDateTime memberRegdate) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNm = memberNm;
        this.memberRegdate = memberRegdate;
    }
}
