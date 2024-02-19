package nineto6.Talk.domain.chatroommember.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndChannelId;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndNickname;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.repository.ChatroomRepository;
import nineto6.Talk.domain.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .chatroomIsGroup(false)
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

    @Test
    void findByChatroomIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        Optional<ChatroomMember> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroom.getChatroomId(), member.getMemberId());

        // then
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(chatroomOptional.get().getChatroomId()).isEqualTo(chatroom.getChatroomId());
    }

    @Test
    void findByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        Optional<ChatroomMember> chatroomMemberOptional = chatroomMemberRepository.findByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // then
        assertThat(chatroomMemberOptional.orElse(null)).isNotNull();
        assertThat(chatroomMemberOptional.get().getChatroomId()).isEqualTo(chatroom.getChatroomId());
        assertThat(chatroomMemberOptional.get().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void updateSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        chatroomMemberRepository.updateSubDateByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // then
        Optional<ChatroomMember> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroom.getChatroomId(), member.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNotNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNull();
    }

    @Test
    void removeSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);
        chatroomMemberRepository.updateSubDateByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // when
        chatroomMemberRepository.removeSubDateByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // then
        Optional<ChatroomMember> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroom.getChatroomId(), member.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNull();
    }

    @Test
    void updateUnSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        chatroomMemberRepository.updateUnSubDateByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // then
        Optional<ChatroomMember> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroom.getChatroomId(), member.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNotNull();
    }

    @Test
    void findOtherUserListByChannelIdAndNickname() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // 다른 사용자 등록
        Member friend = Member.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(friend);

        ChatroomMember chatroomFriend = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(friend.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomFriend);

        // when
        List<ChatroomMemberAndNickname> chatroomMemberList = chatroomMemberRepository.findOtherUserListByChannelIdAndNickname(chatroom.getChatroomChannelId(), member.getMemberNickname());

        // then
        assertThat(chatroomMemberList.size()).isEqualTo(1);
        assertThat(chatroomMemberList.get(0).getMemberId()).isEqualTo(friend.getMemberId());
        assertThat(chatroomMemberList.get(0).getMemberNickname()).isEqualTo(friend.getMemberNickname());
    }

    @Test
    void findByMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMember);

        // when
        List<ChatroomMemberAndChannelId> chatroomMemberList = chatroomMemberRepository.findByMemberId(member.getMemberId());

        // then
        assertThat(chatroomMemberList.size()).isEqualTo(1);
        assertThat(chatroomMemberList.get(0).getChatroomId()).isEqualTo(chatroom.getChatroomId());
        assertThat(chatroomMemberList.get(0).getChannelId()).isEqualTo(chatroom.getChatroomChannelId());
    }
}
