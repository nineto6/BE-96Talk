package nineto6.Talk.domain.chatroom.chatroommember.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMember {
    private Long chatroomMemberId;
    private Long memberId;
    private Long chatroomId;

    @Builder
    public ChatroomMember(Long chatroomMemberId, Long memberId, Long chatroomId) {
        this.chatroomMemberId = chatroomMemberId;
        this.memberId = memberId;
        this.chatroomId = chatroomId;
    }
}
