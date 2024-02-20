package nineto6.Talk.domain.chatroom.repository.mapper;

import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.dto.ChatroomProfileDto;
import nineto6.Talk.domain.profile.dto.ProfileMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatroomMapper {
    void save(Chatroom chatroom);
    Optional<Chatroom> findById(Long chatroomId);
    Optional<Chatroom> findByChannelId(String chatroomChannelId);
    void deleteByChannelId(String chatroomChannelId);
    List<ChatroomProfileDto> findChannelIdAndMemberProfileListByMemberId(Long memberId);
    List<ProfileMemberDto> findMemberProfileByChatroomChannelId(String chatroomChannelId);
    Optional<Chatroom> findChatroomByMemberIdAndFriendId(@Param("memberId") Long memberId, @Param("friendId") Long friendId);
    Optional<Chatroom> findChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
    List<String> findNotFriendInChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
}
