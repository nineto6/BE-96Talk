package nineto6.Talk.domain.chatroom.chatroommember.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMemberAndChannelId;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMemberAndNickname;
import nineto6.Talk.domain.chatroom.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.chatroommember.repository.mapper.ChatroomMemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomMemberRepositoryImpl implements ChatroomMemberRepository {
    private final ChatroomMemberMapper chatroomMemberMapper;
    @Override
    public void save(ChatroomMember chatroomMember) {
        chatroomMemberMapper.save(chatroomMember);
    }

    @Override
    public void deleteByChatroomIdAndMemberId(Long chatroomId, Long memberId) {
        chatroomMemberMapper.deleteByChatroomIdAndMemberId(chatroomId, memberId);
    }

    @Override
    public Optional<ChatroomMember> findById(Long chatroomMemberId) {
        return chatroomMemberMapper.findById(chatroomMemberId);
    }

    @Override
    public Optional<ChatroomMember> findByChatroomIdAndMemberId(Long chatroomId, Long memberId) {
        return chatroomMemberMapper.findByChatroomIdAndMemberId(chatroomId, memberId);
    }

    @Override
    public Optional<ChatroomMember> findByChannelIdAndMemberId(String channelId, Long memberId) {
        return chatroomMemberMapper.findByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public List<ChatroomMemberAndChannelId> findByMemberId(Long memberId) {
        return chatroomMemberMapper.findByMemberId(memberId);
    }

    @Override
    public void updateSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberMapper.updateSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public void removeSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberMapper.removeSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public void updateUnSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberMapper.updateUnSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public List<ChatroomMemberAndNickname> findOtherUserListByChannelIdAndNickname(String channelId, String nickname) {
        return chatroomMemberMapper.findOtherUserListByChannelIdAndNickname(channelId, nickname);
    }
}
