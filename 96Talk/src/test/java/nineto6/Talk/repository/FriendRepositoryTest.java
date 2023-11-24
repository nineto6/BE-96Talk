package nineto6.Talk.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.Friend;
import nineto6.Talk.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
public class FriendRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Test
    void save() {
        // given
        Member member1 = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNm("주인공")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("234324")
                .memberNm("친구")
                .build();
        memberRepository.save(member2);

        Friend friend = Friend.builder()
                .memberId(member1.getMemberId())
                .friendMemberId(member2.getMemberId())
                .build();

        // when
        friendRepository.save(friend);

        // then
        Assertions.assertThat(friend.getFriendId()).isNotNull();
    }

    @Test
    void findByMemberId() {

    }

    @Test
    void deleteByMemberIdAndFriendMemberId() {
        // given
        Member member1 = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNm("주인공")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("234324")
                .memberNm("친구")
                .build();
        memberRepository.save(member2);

        Friend friend = Friend.builder()
                .memberId(member1.getMemberId())
                .friendMemberId(member2.getMemberId())
                .build();
        friendRepository.save(friend);

        // when
        friendRepository.deleteByMemberIdAndFriendMemberId(member1.getMemberId(), member2.getMemberId());

        // then
        List<Friend> friendList = friendRepository.findByMemberId(member1.getMemberId());
        Assertions.assertThat(friendList.size()).isEqualTo(0);
    }
}
