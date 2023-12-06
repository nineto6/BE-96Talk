package nineto6.Talk.domain.chatroom.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.domain.ProfileMember;
import nineto6.Talk.domain.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
public class ChatroomRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatroomMemberRepository chatroomMemberRepository;
    @Autowired
    private ProfileRepository profileRepository;
    private static Chatroom chatroom;
    @BeforeEach
    void setup() {
        chatroom = Chatroom.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname("한국")
                .build();
    }

    @Test
    void save() {
        //given

        // when
        chatroomRepository.save(chatroom);

        // then
        Optional<Chatroom> findChatroom = chatroomRepository.findById(chatroom.getChatroomId());
        assertThat(findChatroom.orElse(null)).isNotNull();
    }

    @Test
    void findById() {
        // given
        chatroomRepository.save(chatroom);

        // when
        Optional<Chatroom> findChatroom = chatroomRepository.findById(chatroom.getChatroomId());

        // then
        assertThat(findChatroom.orElse(null)).isNotNull();
        assertThat(findChatroom.get().getChatroomWriterNickname()).isEqualTo("한국");

    }

    @Test
    void findByChannelId() {
        // given
        chatroomRepository.save(chatroom);

        // when
        Optional<Chatroom> findChatroom = chatroomRepository.findByChannelId(chatroom.getChatroomChannelId());

        // then
        assertThat(findChatroom.orElse(null)).isNotNull();
        assertThat(findChatroom.get().getChatroomWriterNickname()).isEqualTo("한국");
    }

    @Test
    void deleteByChannelId() {
        // given
        chatroomRepository.save(chatroom);

        // when
        chatroomRepository.deleteByChannelId(chatroom.getChatroomChannelId());

        // then
        Optional<Chatroom> chatroomOptional = chatroomRepository.findByChannelId(chatroom.getChatroomChannelId());
        assertThat(chatroomOptional.orElse(null)).isNull();
    }

    @Test
    void findChatroomByMemberId() {
        // given
        chatroomRepository.save(chatroom);

        Member member = Member.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(member);

        ChatroomMember chatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMember);

        // when
        List<Chatroom> chatroomList = chatroomRepository.findChatroomByMemberId(member.getMemberId());

        // then
        assertThat(chatroomList.size()).isEqualTo(1);
        assertThat(chatroomList.get(0).getChatroomChannelId()).isEqualTo(chatroom.getChatroomChannelId());
    }

    @Test
    void findMemberProfileByChatroomChannelId() {
        // given
        chatroomRepository.save(chatroom);

        Member member = Member.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(member);

        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
        profileRepository.saveDefault(profile);
        profileRepository.updateStateMessageByMemberId(member.getMemberId(), "상태메세지");

        ChatroomMember chatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMember);

        // when
        List<ProfileMember> profileMemberList = chatroomRepository.findMemberProfileByChatroomChannelId(chatroom.getChatroomChannelId());

        // then
        assertThat(profileMemberList.size()).isEqualTo(1);
        assertThat(profileMemberList.get(0).getMemberNickname()).isEqualTo("한국");
        assertThat(profileMemberList.get(0).getProfile().getProfileStateMessage()).isEqualTo("상태메세지");
    }
}
