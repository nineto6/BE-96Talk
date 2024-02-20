package nineto6.Talk.domain.chatroommember.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroommember.dto.ChatroomMemberDto;
import nineto6.Talk.domain.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroommember.repository.mapper.ChatroomMemberMapper;
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
    public List<ChatroomMemberDto> findChatroomMemberDtoByMemberId(Long memberId) {
        return chatroomMemberMapper.findChatroomMemberDtoByMemberId(memberId);
    }

    @Override
    public List<ChatroomMemberDto> findOtherChatroomMemberDtoByChannelIdAndNickname(String channelId, String nickname) {
        return chatroomMemberMapper.findOtherChatroomMemberDtoByChannelIdAndNickname(channelId, nickname);
    }
}
