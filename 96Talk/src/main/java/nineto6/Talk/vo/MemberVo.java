package nineto6.Talk.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberVo {
    private Long memberId;
    private String memberEmail;
    private String memberPwd;
    private String memberNickname;
    private LocalDateTime memberRegdate;
    @Builder
    public MemberVo(Long memberId, String memberEmail, String memberPwd, String memberNickname, LocalDateTime memberRegdate) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNickname = memberNickname;
        this.memberRegdate = memberRegdate;
    }
}
