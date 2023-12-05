package nineto6.Talk.domain.friend.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.friend.repository.FriendRepository;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.friend.domain.Friend;
import nineto6.Talk.domain.member.domain.Member;
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
public class FriendRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;

    private static Member member1;
    private static Member member2;
    private static Friend friend;
    @BeforeEach
    void setup() {
        member1 = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNickname("주인공")
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("234324")
                .memberNickname("친구")
                .build();
        memberRepository.save(member2);

        friend = Friend.builder()
                .memberId(member1.getMemberId())
                .friendMemberId(member2.getMemberId())
                .build();
    }

    @Test
    void save() {
        // given

        // when
        friendRepository.save(friend);

        // then
        Assertions.assertThat(friend.getFriendId()).isNotNull();
    }

    @Test
    void findByMemberId() {
        // given
        friendRepository.save(friend);

        // when
        List<Friend> friendOptional = friendRepository.findByMemberId(member1.getMemberId());


        // then
        Assertions.assertThat(friendOptional.size()).isEqualTo(1);
        Assertions.assertThat(friendOptional.get(0).getMemberId()).isEqualTo(member1.getMemberId());
        Assertions.assertThat(friendOptional.get(0).getFriendMemberId()).isEqualTo(member2.getMemberId());
    }

    @Test
    void deleteByMemberIdAndFriendMemberId() {
        // given
        friendRepository.save(friend);

        // when
        friendRepository.deleteByMemberIdAndFriendMemberId(member1.getMemberId(), member2.getMemberId());

        // then
        List<Friend> friendList = friendRepository.findByMemberId(member1.getMemberId());
        Assertions.assertThat(friendList.size()).isEqualTo(0);
    }

    @Test
    void findByMemberIdAndFriendMemberId() {
        // given
        friendRepository.save(friend);

        // when
        Optional<Friend> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(member1.getMemberId(), member2.getMemberId());


        // then
        Friend findFriend = friendOptional.orElse(null);
        Assertions.assertThat(findFriend).isNotNull();
        Assertions.assertThat(findFriend.getMemberId()).isEqualTo(member1.getMemberId());
        Assertions.assertThat(findFriend.getFriendMemberId()).isEqualTo(member2.getMemberId());
    }
}
