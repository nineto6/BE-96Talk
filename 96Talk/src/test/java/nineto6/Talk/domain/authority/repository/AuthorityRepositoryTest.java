package nineto6.Talk.domain.authority.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.authority.code.Role;
import nineto6.Talk.domain.authority.repository.AuthorityRepository;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.authority.domain.Authority;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.domain.MemberAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
public class AuthorityRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    private static Member member;
    private static Authority authority;
    @BeforeEach
    void setup() {
        member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();

        memberRepository.save(member);

        authority = Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build();
    }

    @Test
    @DisplayName("권한 1개 저장 테스트")
    void saveAuthorityList_1() {
        // given

        // when
        authorityRepository.saveAuthority(authority);

        // then
        MemberAuthority findMember = memberRepository.findMemberAndAuthByEmail(member.getMemberEmail()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("권한 2개 저장 테스트")
    void saveAuthorityList_2() {
        // given
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build());
        authorityList.add(Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.ADMIN.getAuth())
                .build());

        // when
        authorityRepository.saveAuthorityList(authorityList);

        // then
        MemberAuthority findMember = memberRepository.findMemberAndAuthByEmail(member.getMemberEmail()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(2);
    }

}
