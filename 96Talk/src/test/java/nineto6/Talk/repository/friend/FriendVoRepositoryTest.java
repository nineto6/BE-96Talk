package nineto6.Talk.repository.friend;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.repository.friend.FriendRepository;
import nineto6.Talk.vo.FriendVo;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.vo.MemberVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
public class FriendVoRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;

    private static MemberVo memberVo1;
    private static MemberVo memberVo2;
    private static FriendVo friendVo;
    @BeforeEach
    void setup() {
        memberVo1 = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNickname("주인공")
                .build();
        memberRepository.save(memberVo1);

        memberVo2 = MemberVo.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("234324")
                .memberNickname("친구")
                .build();
        memberRepository.save(memberVo2);

        friendVo = FriendVo.builder()
                .memberId(memberVo1.getMemberId())
                .friendMemberId(memberVo2.getMemberId())
                .build();
    }

    @Test
    void save() {
        // given

        // when
        friendRepository.save(friendVo);

        // then
        Assertions.assertThat(friendVo.getFriendId()).isNotNull();
    }

    @Test
    void findByMemberId() {
        // given
        friendRepository.save(friendVo);

        // when
        List<FriendVo> friendVoOptional = friendRepository.findByMemberId(memberVo1.getMemberId());


        // then
        Assertions.assertThat(friendVoOptional.size()).isEqualTo(1);
        Assertions.assertThat(friendVoOptional.get(0).getMemberId()).isEqualTo(memberVo1.getMemberId());
        Assertions.assertThat(friendVoOptional.get(0).getFriendMemberId()).isEqualTo(memberVo2.getMemberId());
    }

    @Test
    void deleteByMemberIdAndFriendMemberId() {
        // given
        friendRepository.save(friendVo);

        // when
        friendRepository.deleteByMemberIdAndFriendMemberId(memberVo1.getMemberId(), memberVo2.getMemberId());

        // then
        List<FriendVo> friendVoList = friendRepository.findByMemberId(memberVo1.getMemberId());
        Assertions.assertThat(friendVoList.size()).isEqualTo(0);
    }

    @Test
    void findByMemberIdAndFriendMemberId() {
        // given
        friendRepository.save(friendVo);

        // when
        Optional<FriendVo> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberVo1.getMemberId(), memberVo2.getMemberId());


        // then
        FriendVo findFriendVo = friendOptional.orElse(null);
        Assertions.assertThat(findFriendVo).isNotNull();
        Assertions.assertThat(findFriendVo.getMemberId()).isEqualTo(memberVo1.getMemberId());
        Assertions.assertThat(findFriendVo.getFriendMemberId()).isEqualTo(memberVo2.getMemberId());
    }
}
