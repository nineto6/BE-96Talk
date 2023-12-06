package nineto6.Talk.domain.chatroom.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.chatroom.repository.ChatroomRepository;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.repository.mapper.ChatroomMapper;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.profile.domain.ProfileMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepository {
    private final ChatroomMapper chatroomMapper;
    @Override
    public void save(Chatroom chatroom) {
        chatroomMapper.save(chatroom);
    }

    @Override
    public Optional<Chatroom> findById(Long chatroomId) {
        return chatroomMapper.findById(chatroomId);
    }

    @Override
    public Optional<Chatroom> findByChannelId(String chatroomChannelId) {
        return chatroomMapper.findByChannelId(chatroomChannelId);
    }

    @Override
    public void deleteByChannelId(String chatroomChannelId) {
        chatroomMapper.deleteByChannelId(chatroomChannelId);
    }

    @Override
    public List<Chatroom> findChatroomByMemberId(Long memberId) {
        return chatroomMapper.findChatroomByMemberId(memberId);
    }

    @Override
    public List<ProfileMember> findMemberProfileByChatroomChannelId(String chatroomChannelId){
        return chatroomMapper.findMemberProfileByChatroomChannelId(chatroomChannelId);
    }
}
