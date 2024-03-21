package nineto6.Talk.repository.chatroom;

import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.dto.chatroom.ChatroomProfileDto;
import nineto6.Talk.dto.profile.ProfileMemberDto;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository {
    void save(ChatroomVo chatroomVo);
    Optional<ChatroomVo> findById(Long chatroomId);
    Optional<ChatroomVo> findByChannelId(String chatroomChannelId);
    void deleteByChannelId(String chatroomChannelId);
    List<ChatroomProfileDto> findChannelIdAndMemberProfileListByMemberId(Long memberId);
    List<ProfileMemberDto> findMemberProfileByChatroomChannelId(String chatroomChannelId);
    Optional<ChatroomVo> findChatroomByMemberIdAndFriendId(Long memberId, Long friendId);
    Optional<ChatroomVo> findChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId);
    List<String> findNotFriendInChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId);
}
