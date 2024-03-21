package nineto6.Talk.repository.chatroommember;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.repository.chatroom.ChatroomRepository;
import nineto6.Talk.dto.chatroommember.ChatroomMemberDto;
import nineto6.Talk.repository.member.MemberRepository;
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

    private static ChatroomVo chatroomVo;
    private static MemberVo memberVo;
    private static ChatroomMemberVo chatroomMemberVo;

    @BeforeEach
    void setup() {
        memberVo = MemberVo.builder()
                .memberEmail("test@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(memberVo);

        chatroomVo = ChatroomVo.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname(memberVo.getMemberNickname())
                .chatroomIsGroup(false)
                .build();

        chatroomRepository.save(chatroomVo);

        chatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberVo.getMemberId())
                .build();
    }

    @Test
    void save() {
        // given

        // when
        chatroomMemberRepository.save(chatroomMemberVo);

        // then
        Optional<ChatroomMemberVo> chatroomMemberOptional = chatroomMemberRepository.findById(chatroomMemberVo.getChatroomMemberId());
        assertThat(chatroomMemberOptional.orElse(null)).isNotNull();
        assertThat(chatroomMemberOptional.get().getChatroomId()).isEqualTo(chatroomVo.getChatroomId());
        assertThat(chatroomMemberOptional.get().getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    void deleteByChatroomIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        chatroomMemberRepository.deleteByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberVo.getMemberId());

        // then
        Optional<ChatroomMemberVo> chatroomMemberOptional = chatroomMemberRepository.findById(chatroomMemberVo.getChatroomMemberId());
        assertThat(chatroomMemberOptional.orElse(null)).isNull();
    }

    @Test
    void findByChatroomIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        Optional<ChatroomMemberVo> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberVo.getMemberId());

        // then
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getMemberId()).isEqualTo(memberVo.getMemberId());
        assertThat(chatroomOptional.get().getChatroomId()).isEqualTo(chatroomVo.getChatroomId());
    }

    @Test
    void findByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        Optional<ChatroomMemberVo> chatroomMemberOptional = chatroomMemberRepository.findByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // then
        assertThat(chatroomMemberOptional.orElse(null)).isNotNull();
        assertThat(chatroomMemberOptional.get().getChatroomId()).isEqualTo(chatroomVo.getChatroomId());
        assertThat(chatroomMemberOptional.get().getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    void updateSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        chatroomMemberRepository.updateSubDateByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // then
        Optional<ChatroomMemberVo> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberVo.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNotNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNull();
    }

    @Test
    void removeSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);
        chatroomMemberRepository.updateSubDateByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // when
        chatroomMemberRepository.removeSubDateByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // then
        Optional<ChatroomMemberVo> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberVo.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNull();
    }

    @Test
    void updateUnSubDateByChannelIdAndMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        chatroomMemberRepository.updateUnSubDateByChannelIdAndMemberId(chatroomVo.getChatroomChannelId(), memberVo.getMemberId());

        // then
        Optional<ChatroomMemberVo> chatroomOptional = chatroomMemberRepository.findByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberVo.getMemberId());
        assertThat(chatroomOptional.orElse(null)).isNotNull();
        assertThat(chatroomOptional.get().getChatroomSubDate()).isNull();
        assertThat(chatroomOptional.get().getChatroomUnSubDate()).isNotNull();
    }

    @Test
    void findOtherChatroomMemberDtoByChannelIdAndNickname() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // 다른 사용자 등록
        MemberVo friend = MemberVo.builder()
                .memberEmail("test1@naver.com")
                .memberPwd("123123")
                .memberNickname("한국1")
                .build();
        memberRepository.save(friend);

        ChatroomMemberVo chatroomFriend = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(friend.getMemberId())
                .build();
        chatroomMemberRepository.save(chatroomFriend);

        // when
        List<ChatroomMemberDto> chatroomMemberList = chatroomMemberRepository.findOtherChatroomMemberDtoByChannelIdAndNickname(chatroomVo.getChatroomChannelId(), memberVo.getMemberNickname());

        // then
        assertThat(chatroomMemberList.size()).isEqualTo(1);
        assertThat(chatroomMemberList.get(0).getMemberNickname()).isEqualTo(friend.getMemberNickname());
    }

    @Test
    void findChatroomMemberDtoByMemberId() {
        // given
        chatroomMemberRepository.save(chatroomMemberVo);

        // when
        List<ChatroomMemberDto> chatroomMemberList = chatroomMemberRepository.findChatroomMemberDtoByMemberId(memberVo.getMemberId());

        // then
        assertThat(chatroomMemberList.size()).isEqualTo(1);
        assertThat(chatroomMemberList.get(0).getChannelId()).isEqualTo(chatroomVo.getChatroomChannelId());
    }
}
