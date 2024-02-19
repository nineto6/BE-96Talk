package nineto6.Talk.global.chat.mongodb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequest {
    private String channelId;
    private String message;
    private String writerNickname;

    @Builder
    public ChatRequest(String channelId, String message, String writerNickname) {
        this.channelId = channelId;
        this.message = message;
        this.writerNickname = writerNickname;
    }
}
