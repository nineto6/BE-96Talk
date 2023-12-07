package nineto6.Talk.domain.chatroom.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nineto6.Talk.domain.profile.domain.ProfileMember;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomProfile {
    private String chatroomChannelId;
    private List<ProfileMember> memberProfileList;

    @Builder
    public ChatroomProfile(String chatroomChannelId, List<ProfileMember> memberProfileList) {
        this.chatroomChannelId = chatroomChannelId;
        this.memberProfileList = memberProfileList;
    }
}
