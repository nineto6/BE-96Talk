package nineto6.Talk.repository.chatroom.dao;

import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.dto.chatroom.ChatroomProfileDto;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatroomDao {
    void save(ChatroomVo chatroomVo);
    Optional<ChatroomVo> findById(Long chatroomId);
    Optional<ChatroomVo> findByChannelId(String chatroomChannelId);
    void deleteByChannelId(String chatroomChannelId);
    List<ChatroomProfileDto> findChannelIdAndMemberProfileListByMemberId(Long memberId);
    List<ProfileMemberDto> findMemberProfileByChatroomChannelId(String chatroomChannelId);
    Optional<ChatroomVo> findChatroomByMemberIdAndFriendId(@Param("memberId") Long memberId, @Param("friendId") Long friendId);
    Optional<ChatroomVo> findChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
    List<String> findNotFriendInChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
}
