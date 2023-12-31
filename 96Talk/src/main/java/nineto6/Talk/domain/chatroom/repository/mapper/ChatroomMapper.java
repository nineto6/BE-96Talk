package nineto6.Talk.domain.chatroom.repository.mapper;

import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.domain.ChatroomProfile;
import nineto6.Talk.domain.profile.domain.ProfileMember;
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
    List<ChatroomProfile> findChannelIdAndMemberProfileListByMemberId(Long memberId);
    List<ProfileMember> findMemberProfileByChatroomChannelId(String chatroomChannelId);
    Optional<Chatroom> findChatroomByMemberIdAndFriendId(@Param("memberId") Long memberId, @Param("friendId") Long friendId);
    Optional<Chatroom> findChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
    List<String> findNotFriendInChatroomByChannelIdAndMemberId(@Param("chatroomChannelId") String chatroomChannelId, @Param("memberId") Long memberId);
}
