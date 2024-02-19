package nineto6.Talk.domain.chatroommember.repository;

import nineto6.Talk.domain.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndChannelId;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndNickname;

import java.util.List;
import java.util.Optional;

public interface ChatroomMemberRepository {
    void save(ChatroomMember chatroomMember);
    void deleteByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<ChatroomMember> findById(Long chatroomMemberId);
    Optional<ChatroomMember> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<ChatroomMember> findByChannelIdAndMemberId(String channelId, Long memberId);
    List<ChatroomMemberAndChannelId> findByMemberId(Long memberId);
    void updateSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    void removeSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    void updateUnSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    List<ChatroomMemberAndNickname> findOtherUserListByChannelIdAndNickname(String channelId, String nickname);
}
