package nineto6.Talk.controller.chatroom.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nineto6.Talk.global.common.code.SuccessCode;

@Getter @Setter
public class ChatroomSaveResponse {
    private String chatroomChannelId;
    private SuccessCode successCode;

    @Builder
    public ChatroomSaveResponse(String chatroomChannelId, SuccessCode successCode) {
        this.chatroomChannelId = chatroomChannelId;
        this.successCode = successCode;
    }
}
