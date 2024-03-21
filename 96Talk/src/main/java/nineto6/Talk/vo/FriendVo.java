package nineto6.Talk.vo;

import lombok.*;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendVo {
    private Long friendId;
    private Long memberId;
    private Long friendMemberId;

    @Builder
    public FriendVo(Long friendId, Long memberId, Long friendMemberId) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
    }
}
