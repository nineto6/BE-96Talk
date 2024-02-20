package nineto6.Talk.domain.chatroom.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nineto6.Talk.domain.profile.dto.ProfileMemberDto;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomProfileDto {
    private String chatroomChannelId;
    private List<ProfileMemberDto> memberProfileList;

    @Builder
    public ChatroomProfileDto(String chatroomChannelId, List<ProfileMemberDto> memberProfileList) {
        this.chatroomChannelId = chatroomChannelId;
        this.memberProfileList = memberProfileList;
    }
}
