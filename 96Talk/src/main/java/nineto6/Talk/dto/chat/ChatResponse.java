package nineto6.Talk.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class ChatResponse {
    private String channelId;
    private String message;
    private String writerNickname;
    private LocalDateTime regdate;

    @Builder
    public ChatResponse(String channelId, String message, String writerNickname, LocalDateTime regdate) {
        this.channelId = channelId;
        this.message = message;
        this.writerNickname = writerNickname;
        this.regdate = regdate;
    }
}
