package nineto6.Talk.domain.chatroom.chatroommember.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.repository.ChatroomRepository;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class ChatroomMemberRepositoryTest {
    @Autowired
    private ChatroomMemberRepository chatroomMemberRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    private static Chatroom chatroom;
    private static Member member;
    private static ChatroomMember chatroomMember;

    @BeforeEach
    void setup() {
        member = Member.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(member);

        chatroom = Chatroom.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname(member.getMemberNickname())
                .build();

        chatroomRepository.save(chatroom);

        chatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member.getMemberId())
                .build();
    }

    @Test
    void save() {
        // given

        // when
        chatroomMemberRepository.save(chatroomMember);

        // then
        Optional<ChatroomMember> chatroomMemberOptional = chatroomMemberRepository.findById(chatroomMember.getChatroomMemberId());
        assertThat(chatroomMemberOptional.orElse(null)).isNotNull();
        assertThat(chatroomMemberOptional.get().getChatroomId()).isEqualTo(chatroom.getChatroomId());
        assertThat(chatroomMemberOptional.get().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void deleteByChatroomIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        chatroomMemberRepository.deleteByChatroomIdAndMemberId(chatroom.getChatroomId(), member.getMemberId());

        // then
        Optional<ChatroomMember> chatroomMemberOptional = chatroomMemberRepository.findById(chatroomMember.getChatroomMemberId());
        assertThat(chatroomMemberOptional.orElse(null)).isNull();
    }
}
