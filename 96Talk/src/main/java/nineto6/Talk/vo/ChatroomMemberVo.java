package nineto6.Talk.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMemberVo {
    private Long chatroomMemberId;
    private Long memberId;
    private Long chatroomId;
    private LocalDateTime chatroomSubDate;
    private LocalDateTime chatroomUnSubDate;

    @Builder
    public ChatroomMemberVo(Long chatroomMemberId, Long memberId, Long chatroomId, LocalDateTime chatroomSubDate, LocalDateTime chatroomUnSubDate) {
        this.chatroomMemberId = chatroomMemberId;
        this.memberId = memberId;
        this.chatroomId = chatroomId;
        this.chatroomSubDate = chatroomSubDate;
        this.chatroomUnSubDate = chatroomUnSubDate;
    }
}
