package nineto6.Talk.domain.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
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
