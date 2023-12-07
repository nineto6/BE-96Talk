package nineto6.Talk.domain.chatroom.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.domain.ChatroomProfile;
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
    private Chatroom chatroom;
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
    void findChannelIdAndProfileListByMemberId() {
        // given
        chatroomRepository.save(chatroom);
        log.info("chatroom 등록 : {}", chatroom.getChatroomId());
        // 자신 데이터 등록
        Member member = Member.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(member);
        log.info("자신 데이터 등록 : {}", member.getMemberId());
        profileRepository.saveDefault(Profile.builder()
                .memberId(member.getMemberId())
                .build());

        chatroomMemberRepository.save(ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member.getMemberId())
                .build());

        log.info("처음 setup 된 채팅방에 멤버 리스트 등록");
        // 처음 setup 된 채팅방에 멤버 리스트 등록
        for (int i = 0; i < 2; i++) {
            Member forMember = Member.builder()
                    .memberEmail("test1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("한국1" + i)
                    .build();
            memberRepository.save(forMember);
            log.info("멤버 등록 : {}", forMember.getMemberId());

            profileRepository.saveDefault(Profile.builder()
                    .memberId(forMember.getMemberId())
                    .build());

            chatroomMemberRepository.save(ChatroomMember.builder()
                    .chatroomId(chatroom.getChatroomId())
                    .memberId(forMember.getMemberId())
                    .build());
        }
        log.info("2번째 채팅방 생성");
        // 2번째 채팅방 생성
        Chatroom chatroom2 = Chatroom.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname("한국")
                .build();
        chatroomRepository.save(chatroom2);

        log.info("2번째 채팅방에 멤버 등록");
        // 2번째 채팅방에 멤버 등록
        chatroomMemberRepository.save(ChatroomMember.builder()
                .chatroomId(chatroom2.getChatroomId())
                .memberId(member.getMemberId())
                .build());

        log.info("2번째 채팅방에 멤버 리스트 등록");
        // 2번째 채팅방에 멤버 등록
        for (int i = 0; i < 2; i++) {
            Member forMember = Member.builder()
                    .memberEmail("test2" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("한국2" + i)
                    .build();
            memberRepository.save(forMember);
            log.info("멤버 등록 : {}", forMember.getMemberId());

            profileRepository.saveDefault(Profile.builder()
                    .memberId(forMember.getMemberId())
                    .build());

            chatroomMemberRepository.save(ChatroomMember.builder()
                    .chatroomId(chatroom2.getChatroomId())
                    .memberId(forMember.getMemberId())
                    .build());
        }

        // when
        List<ChatroomProfile> chatroomProfileList = chatroomRepository.findChannelIdAndProfileListByMemberId(member.getMemberId());

        // then
        // 채팅방은 2개가 되어야 한다.
        assertThat(chatroomProfileList.size()).isEqualTo(2);
        // 각각 채팅방안에 프로필은 자신을 뺀 나머지 2명 조회가 되어야 한다.
        assertThat(chatroomProfileList.get(0).getMemberProfileList().size()).isEqualTo(2);
        assertThat(chatroomProfileList.get(1).getMemberProfileList().size()).isEqualTo(2);
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
