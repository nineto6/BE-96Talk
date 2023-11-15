package nineto6.Talk.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.Role;
import nineto6.Talk.domain.Authority;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
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

    @Test
    @DisplayName("권한 1개 저장 테스트")
    void saveAuthorityList_1() {
        // given
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();

        memberRepository.save(member);

        Authority authority = Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build();
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
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();

        memberRepository.save(member);

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