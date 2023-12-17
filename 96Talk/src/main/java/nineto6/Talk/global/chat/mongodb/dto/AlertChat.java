package nineto6.Talk.global.chat.mongodb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlertChat {
    private String channelId;
    private Long count;

    @Builder
    public AlertChat(String channelId, Long count) {
        this.channelId = channelId;
        this.count = count;
    }
}
