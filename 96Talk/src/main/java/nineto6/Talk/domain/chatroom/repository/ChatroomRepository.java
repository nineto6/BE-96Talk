package nineto6.Talk.domain.chatroom.repository;

import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.domain.ChatroomProfile;
import nineto6.Talk.domain.profile.domain.ProfileMember;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository {
    void save(Chatroom chatroom);
    Optional<Chatroom> findById(Long chatroomId);
    Optional<Chatroom> findByChannelId(String chatroomChannelId);
    void deleteByChannelId(String chatroomChannelId);
    List<ChatroomProfile> findChannelIdAndMemberProfileListByMemberId(Long memberId);
    List<ProfileMember> findMemberProfileByChatroomChannelId(String chatroomChannelId);
    Optional<Chatroom> findChatroomByMemberIdAndFriendId(Long memberId, Long friendId);
    Optional<Chatroom> findChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId);
    List<String> findNotFriendInChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId);
}
