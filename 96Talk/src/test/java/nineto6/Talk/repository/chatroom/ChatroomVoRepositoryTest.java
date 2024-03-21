package nineto6.Talk.repository.chatroom;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.repository.chatroom.ChatroomRepository;
import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.repository.chatroommember.ChatroomMemberRepository;
import nineto6.Talk.dto.chatroom.ChatroomProfileDto;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.repository.profile.ProfileRepository;
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
public class ChatroomVoRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatroomMemberRepository chatroomMemberRepository;
    @Autowired
    private ProfileRepository profileRepository;
    private ChatroomVo chatroomVo;
    @BeforeEach
    void setup() {
        chatroomVo = ChatroomVo.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname("한국")
                .chatroomIsGroup(false)
                .build();
    }

    @Test
    void save() {
        //given

        // when
        chatroomRepository.save(chatroomVo);

        // then
        Optional<ChatroomVo> findChatroom = chatroomRepository.findById(chatroomVo.getChatroomId());
        assertThat(findChatroom.orElse(null)).isNotNull();
    }

    @Test
    void findById() {
        // given
        chatroomRepository.save(chatroomVo);

        // when
        Optional<ChatroomVo> findChatroom = chatroomRepository.findById(chatroomVo.getChatroomId());

        // then
        assertThat(findChatroom.orElse(null)).isNotNull();
        assertThat(findChatroom.get().getChatroomWriterNickname()).isEqualTo("한국");

    }

    @Test
    void findByChannelId() {
        // given
        chatroomRepository.save(chatroomVo);

        // when
        Optional<ChatroomVo> findChatroom = chatroomRepository.findByChannelId(chatroomVo.getChatroomChannelId());

        // then
        assertThat(findChatroom.orElse(null)).isNotNull();
        assertThat(findChatroom.get().getChatroomWriterNickname()).isEqualTo("한국");
    }

    @Test
    void deleteByChannelId() {
        // given
        chatroomRepository.save(chatroomVo);

        // when
        chatroomRepository.deleteByChannelId(chatroomVo.getChatroomChannelId());

        // then
        Optional<ChatroomVo> chatroomOptional = chatroomRepository.findByChannelId(chatroomVo.getChatroomChannelId());
        assertThat(chatroomOptional.orElse(null)).isNull();
    }

    @Test
    void findChannelIdAndProfileListByMemberId() {
        // given
        chatroomRepository.save(chatroomVo);
        log.info("chatroom 등록 : {}", chatroomVo.getChatroomId());

        // 자신 데이터 등록
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(memberVo);
        log.info("자신 데이터 등록 : {}", memberVo.getMemberId());

        profileRepository.saveDefault(ProfileVo.builder()
                .memberId(memberVo.getMemberId())
                .build());

        chatroomMemberRepository.save(ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build());

        log.info("처음 setup 된 채팅방에 멤버 리스트 등록");
        // 처음 setup 된 채팅방에 멤버 리스트 등록
        for (int i = 0; i < 2; i++) {
            MemberVo forMemberVo = MemberVo.builder()
                    .memberEmail("test1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("한국1" + i)
                    .build();
            memberRepository.save(forMemberVo);
            log.info("멤버 등록 : {}", forMemberVo.getMemberId());

            profileRepository.saveDefault(ProfileVo.builder()
                    .memberId(forMemberVo.getMemberId())
                    .build());

            chatroomMemberRepository.save(ChatroomMemberVo.builder()
                    .chatroomId(chatroomVo.getChatroomId())
                    .memberId(forMemberVo.getMemberId())
                    .build());
        }

        log.info("2번째 채팅방 생성");
        // 2번째 채팅방 생성
        ChatroomVo chatroomVo2 = ChatroomVo.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname("한국")
                .chatroomIsGroup(false)
                .build();
        chatroomRepository.save(chatroomVo2);

        log.info("2번째 채팅방에 멤버 등록");
        // 2번째 채팅방에 멤버 등록
        chatroomMemberRepository.save(ChatroomMemberVo.builder()
                .chatroomId(chatroomVo2.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build());

        log.info("2번째 채팅방에 멤버 리스트 등록");
        // 2번째 채팅방에 멤버 등록
        for (int i = 0; i < 2; i++) {
            MemberVo forMemberVo = MemberVo.builder()
                    .memberEmail("test2" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("한국2" + i)
                    .build();
            memberRepository.save(forMemberVo);
            log.info("멤버 등록 : {}", forMemberVo.getMemberId());

            profileRepository.saveDefault(ProfileVo.builder()
                    .memberId(forMemberVo.getMemberId())
                    .build());

            chatroomMemberRepository.save(ChatroomMemberVo.builder()
                    .chatroomId(chatroomVo2.getChatroomId())
                    .memberId(forMemberVo.getMemberId())
                    .build());
        }

        // when
        List<ChatroomProfileDto> chatroomProfileDtoList = chatroomRepository.findChannelIdAndMemberProfileListByMemberId(memberVo.getMemberId());

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
        chatroomRepository.save(chatroomVo);

        MemberVo memberVo = MemberVo.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(memberVo);

        ProfileVo profileVo = ProfileVo.builder()
                .memberId(memberVo.getMemberId())
                .build();
        profileRepository.saveDefault(profileVo);
        profileRepository.updateStateMessageByMemberId(memberVo.getMemberId(), "상태메세지");

        ChatroomMemberVo chatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        List<ProfileMemberDto> profileMemberDtoList = chatroomRepository.findMemberProfileByChatroomChannelId(chatroomVo.getChatroomChannelId());

        // then
        assertThat(profileMemberDtoList.size()).isEqualTo(1);
        assertThat(profileMemberDtoList.get(0).getMemberNickname()).isEqualTo("한국");
        assertThat(profileMemberDtoList.get(0).getProfileVo().getProfileStateMessage()).isEqualTo("상태메세지");
    }

    @Test
    void findChatroomByMemberIdAndFriendId() {
        // given
        chatroomRepository.save(chatroomVo);

        // 자신 등록
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(memberVo);

        ProfileVo profileVo = ProfileVo.builder()
                .memberId(memberVo.getMemberId())
                .build();
        profileRepository.saveDefault(profileVo);

        ChatroomMemberVo chatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMemberVo);

        // 친구 등록
        MemberVo friend = MemberVo.builder()
                .memberEmail("test2@naver.com")
                .memberPwd("123123")
                .memberNickname("한국2")
                .build();
        memberRepository.save(friend);

        ProfileVo friendProfileVo = ProfileVo.builder()
                .memberId(friend.getMemberId())
                .build();
        profileRepository.saveDefault(friendProfileVo);

        ChatroomMemberVo friendChatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(friend.getMemberId())
                .build();
        chatroomMemberRepository.save(friendChatroomMemberVo);

        // when
        Optional<ChatroomVo> chatroomOptional = chatroomRepository.findChatroomByMemberIdAndFriendId(memberVo.getMemberId(), friend.getMemberId());

        // then
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomChannelId()).isEqualTo(chatroomVo.getChatroomChannelId());
    }

    @Test
    void findChatroomByChannelIdAndMemberId() {
        // given
        chatroomRepository.save(chatroomVo);

        MemberVo memberVo = MemberVo.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(memberVo);

        ChatroomMemberVo chatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        Optional<ChatroomVo> chatroomOptional = chatroomRepository.findChatroomByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // then
        // 자신이 속한 채팅방일 경우
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomChannelId()).isEqualTo(chatroomVo.getChatroomChannelId());
    }

    @Test
    void findNotFriendInChatroomByChannelIdAndMemberId() {
        // given
        chatroomRepository.save(chatroomVo);

        MemberVo memberVo1 = MemberVo.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(memberVo1);

        // 채팅방멤버에 등록
        ChatroomMemberVo chatroomMemberVo1 = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo1.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMemberVo1);

        // 친구 등록 안함
        MemberVo memberVo2 = MemberVo.builder()
                .memberEmail("test2@naver.com")
                .memberPwd("123123")
                .memberNickname("한국2")
                .build();
        memberRepository.save(memberVo2);

        // 채팅방멤버에 등록
        ChatroomMemberVo chatroomMemberVo2 = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo2.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomMemberVo2);

        // when
        List<String> nicknameList = chatroomRepository.findNotFriendInChatroomByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo1.getMemberId());

        // then
        assertThat(nicknameList.size()).isEqualTo(1);
        assertThat(nicknameList.get(0)).isEqualTo("한국2");
    }
}
