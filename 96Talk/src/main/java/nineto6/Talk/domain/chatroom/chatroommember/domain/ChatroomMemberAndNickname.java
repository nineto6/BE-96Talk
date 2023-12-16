package nineto6.Talk.domain.chatroom.chatroommember.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMemberAndNickname {
    private Long chatroomMemberId;
    private Long memberId;
    private Long chatroomId;
    private LocalDateTime chatroomSubDate;
    private LocalDateTime chatroomUnSubDate;
    private String memberNickname;
    @Builder
    public ChatroomMemberAndNickname(Long chatroomMemberId, Long memberId, Long chatroomId, LocalDateTime chatroomSubDate, LocalDateTime chatroomUnSubDate, String memberNickname) {
        this.chatroomMemberId = chatroomMemberId;
        this.memberId = memberId;
        this.chatroomId = chatroomId;
        this.chatroomSubDate = chatroomSubDate;
        this.chatroomUnSubDate = chatroomUnSubDate;
        this.memberNickname = memberNickname;
    }
}
