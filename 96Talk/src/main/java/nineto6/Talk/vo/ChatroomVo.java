package nineto6.Talk.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomVo {
    private Long chatroomId;
    private String chatroomChannelId;
    private String chatroomWriterNickname;
    private String chatroomTitle;
    private Boolean chatroomIsGroup;
    private LocalDateTime chatroomRegdate;

    @Builder
    public ChatroomVo(Long chatroomId, String chatroomChannelId, String chatroomWriterNickname, String chatroomTitle, Boolean chatroomIsGroup, LocalDateTime chatroomRegdate) {
        this.chatroomId = chatroomId;
        this.chatroomChannelId = chatroomChannelId;
        this.chatroomWriterNickname = chatroomWriterNickname;
        this.chatroomTitle = chatroomTitle;
        this.chatroomIsGroup = chatroomIsGroup;
        this.chatroomRegdate = chatroomRegdate;
    }
}
