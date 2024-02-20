package nineto6.Talk.domain.chatroom.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.dto.ChatroomProfileDto;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.dto.ProfileMemberDto;
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
                .chatroomIsGroup(false)
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
                .chatroomIsGroup(false)
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
        List<ChatroomProfileDto> chatroomProfileDtoList = chatroomRepository.findChannelIdAndMemberProfileListByMemberId(member.getMemberId());

        // then
        // 채팅방은 2개가 되어야 한다.
        assertThat(chatroomProfileDtoList.size()).isEqualTo(2);
        // 각각 채팅방안에 프로필은 자신을 뺀 나머지 2명 조회가 되어야 한다.
        assertThat(chatroomProfileDtoList.get(0).getMemberProfileList().size()).isEqualTo(2);
        assertThat(chatroomProfileDtoList.get(1).getMemberProfileList().size()).isEqualTo(2);
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
        List<ProfileMemberDto> profileMemberDtoList = chatroomRepository.findMemberProfileByChatroomChannelId(chatroom.getChatroomChannelId());

        // then
        assertThat(profileMemberDtoList.size()).isEqualTo(1);
        assertThat(profileMemberDtoList.get(0).getMemberNickname()).isEqualTo("한국");
        assertThat(profileMemberDtoList.get(0).getProfile().getProfileStateMessage()).isEqualTo("상태메세지");
    }

    @Test
    void findChatroomByMemberIdAndFriendId() {
        // given
        chatroomRepository.save(chatroom);

        // 자신 등록
        Member member = Member.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(member);

        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
        profileRepository.saveDefault(profile);

        ChatroomMember chatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMember);

        // 친구 등록
        Member friend = Member.builder()
                .memberEmail("test2@naver.com")
                .memberPwd("123123")
                .memberNickname("한국2")
                .build();
        memberRepository.save(friend);

        Profile friendProfile = Profile.builder()
                .memberId(friend.getMemberId())
                .build();
        profileRepository.saveDefault(friendProfile);

        ChatroomMember friendChatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(friend.getMemberId())
                .build();
        chatroomMemberRepository.save(friendChatroomMember);

        // when
        Optional<Chatroom> chatroomOptional = chatroomRepository.findChatroomByMemberIdAndFriendId(member.getMemberId(), friend.getMemberId());

        // then
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomChannelId()).isEqualTo(chatroom.getChatroomChannelId());
    }

    @Test
    void findChatroomByChannelIdAndMemberId() {
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
        Optional<Chatroom> chatroomOptional = chatroomRepository.findChatroomByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member.getMemberId());

        // then
        // 자신이 속한 채팅방일 경우
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomChannelId()).isEqualTo(chatroom.getChatroomChannelId());
    }

    @Test
    void findNotFriendInChatroomByChannelIdAndMemberId() {
        // given
        chatroomRepository.save(chatroom);

        Member member1 = Member.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(member1);

        // 채팅방멤버에 등록
        ChatroomMember chatroomMember1 = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member1.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMember1);

        // 친구 등록 안함
        Member member2 = Member.builder()
                .memberEmail("test2@naver.com")
                .memberPwd("123123")
                .memberNickname("한국2")
                .build();
        memberRepository.save(member2);

        // 채팅방멤버에 등록
        ChatroomMember chatroomMember2 = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(member2.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMember2);

        // when
        List<String> nicknameList = chatroomRepository.findNotFriendInChatroomByChannelIdAndMemberId(chatroom.getChatroomChannelId(), member1.getMemberId());

        // then
        assertThat(nicknameList.size()).isEqualTo(1);
        assertThat(nicknameList.get(0)).isEqualTo("한국2");
    }
}
