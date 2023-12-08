package nineto6.Talk.domain.chatroom.chatroommember.repository;

import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;

import java.util.Optional;

public interface ChatroomMemberRepository {
    void save(ChatroomMember chatroomMember);
    void deleteByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<ChatroomMember> findById(Long chatroomMemberId);
    Optional<ChatroomMember> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);
}
