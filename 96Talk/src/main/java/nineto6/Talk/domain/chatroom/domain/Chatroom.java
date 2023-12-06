package nineto6.Talk.domain.chatroom.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {
    private Long chatroomId;
    private String chatroomChannelId;
    private String chatroomWriterNickname;
    private String chatroomTitle;
    private LocalDateTime chatroomRegdate;

    @Builder
    public Chatroom(Long chatroomId, String chatroomChannelId, String chatroomWriterNickname, String chatroomTitle, LocalDateTime chatroomRegdate) {
        this.chatroomId = chatroomId;
        this.chatroomChannelId = chatroomChannelId;
        this.chatroomWriterNickname = chatroomWriterNickname;
        this.chatroomTitle = chatroomTitle;
        this.chatroomRegdate = chatroomRegdate;
    }
}
