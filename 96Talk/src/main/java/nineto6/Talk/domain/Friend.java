package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    private Long friendId;
    private Long memberId;
    private Long friendMemberId;

    @Builder
    public Friend(Long friendId, Long memberId, Long friendMemberId) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
    }
}
