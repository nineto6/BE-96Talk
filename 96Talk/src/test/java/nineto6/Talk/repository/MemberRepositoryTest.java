package nineto6.Talk.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.Role;
import nineto6.Talk.domain.Authority;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 저장 테스트")
    void save() {
        // given
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();

        // when
        memberRepository.save(member);

        // then
        assertThat(member.getMemberId()).isNotNull();
    }

    @Test
    void findByMemberEmail() {
        // given
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> findMemberOptional = memberRepository.findByMemberEmail(member.getMemberEmail());

        // then
        Member findMember = findMemberOptional.orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberEmail()).isEqualTo("hello@naver.com");
    }

    @Test
    void  findByMemberNm() {
        // given
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> findMemberOptional = memberRepository.findByMemberNm(member.getMemberNm());

        // then
        Member findMember = findMemberOptional.orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberNm()).isEqualTo("한국");
    }

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
        memberRepository.saveAuthority(authority);

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
        memberRepository.saveAuthorityList(authorityList);

        // then
        MemberAuthority findMember = memberRepository.findMemberAndAuthByEmail(member.getMemberEmail()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버와 권한(List) Join 조회 테스트")
    void findMemberAndAuthByEmail() {
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
        memberRepository.saveAuthorityList(authorityList);

        // when
        MemberAuthority findMember = memberRepository.findMemberAndAuthByEmail(member.getMemberEmail()).orElse(null);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(2);
        assertThat(findMember.getMemberEmail()).isEqualTo("hello@naver.com");
    }
}
