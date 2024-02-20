package nineto6.Talk.domain.chatroommember.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMemberDto {
    private String channelId;
    private String memberNickname;
    private LocalDateTime chatroomSubDate;
    private LocalDateTime chatroomUnSubDate;
    @Builder
    public ChatroomMemberDto(String channelId, String memberNickname, LocalDateTime chatroomSubDate, LocalDateTime chatroomUnSubDate) {
        this.channelId = channelId;
        this.memberNickname = memberNickname;
        this.chatroomSubDate = chatroomSubDate;
        this.chatroomUnSubDate = chatroomUnSubDate;
    }
}
