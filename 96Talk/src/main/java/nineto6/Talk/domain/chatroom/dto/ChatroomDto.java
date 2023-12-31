package nineto6.Talk.domain.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nineto6.Talk.domain.profile.dto.ProfileResponse;
import nineto6.Talk.domain.chat.mongodb.dto.RecentChat;

import java.util.List;

@Getter @Setter
public class ChatroomDto {
    private String chatroomChannelId;
    private RecentChat recentChat;
    private List<ProfileResponse> profileResponseList;

    @Builder
    public ChatroomDto(String chatroomChannelId, RecentChat recentChat, List<ProfileResponse> profileResponseList) {
        this.chatroomChannelId = chatroomChannelId;
        this.recentChat = recentChat;
        this.profileResponseList = profileResponseList;
    }
}
