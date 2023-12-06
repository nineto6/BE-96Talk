package nineto6.Talk.domain.chatroom.chatroommember.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.chatroommember.repository.mapper.ChatroomMemberMapper;
import org.springframework.stereotype.Repository;

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
}
