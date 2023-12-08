package nineto6.Talk.domain.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nineto6.Talk.domain.profile.dto.ProfileResponse;

import java.util.List;

@Getter @Setter
public class ChatroomDto {
    private String chatroomChannelId;
    private String recentMessage;
    private List<ProfileResponse> profileResponseList;

    @Builder
    public ChatroomDto(String chatroomChannelId, String recentMessage, List<ProfileResponse> profileResponseList) {
        this.chatroomChannelId = chatroomChannelId;
        this.recentMessage = recentMessage;
        this.profileResponseList = profileResponseList;
    }
}
