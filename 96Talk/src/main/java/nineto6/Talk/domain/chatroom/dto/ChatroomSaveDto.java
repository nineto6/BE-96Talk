package nineto6.Talk.domain.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nineto6.Talk.global.common.code.SuccessCode;

@Getter @Setter
public class ChatroomSaveDto {
    private String chatroomChannelId;
    private SuccessCode successCode;

    @Builder
    public ChatroomSaveDto(String chatroomChannelId, SuccessCode successCode) {
        this.chatroomChannelId = chatroomChannelId;
        this.successCode = successCode;
    }
}
