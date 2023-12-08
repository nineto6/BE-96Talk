package nineto6.Talk.domain.chatroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(title = "채팅방 생성")
public class ChatroomSaveRequest {
    @Schema(title = "친구 닉네임")
    private String friendNickname;

    @Builder
    public ChatroomSaveRequest(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}
