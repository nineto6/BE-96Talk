package nineto6.Talk.domain.chatroom.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(title = "채팅방 삭제")
public class ChatroomDeleteRequest {
    @Schema(title = "채팅방 채널 아이디")
    private String channelId;

    @Builder
    public ChatroomDeleteRequest(String channelId) {
        this.channelId = channelId;
    }
}
