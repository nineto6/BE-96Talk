package nineto6.Talk.domain.chatroom.repository;

import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.profile.domain.ProfileMember;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository {
    void save(Chatroom chatroom);
    Optional<Chatroom> findById(Long chatroomId);
    Optional<Chatroom> findByChannelId(String chatroomChannelId);
    void deleteByChannelId(String chatroomChannelId);
    List<Chatroom> findChatroomByMemberId(Long memberId);
    List<ProfileMember> findMemberProfileByChatroomChannelId(String chatroomChannelId);
}
