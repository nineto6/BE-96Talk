package nineto6.Talk.repository.member;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.dto.authority.code.Role;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.vo.AuthorityVo;
import nineto6.Talk.repository.authority.AuthorityRepository;
import nineto6.Talk.dto.member.MemberAuthorityDto;
import org.junit.jupiter.api.BeforeEach;
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
public class MemberVoRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    private static MemberVo memberVo;

    @BeforeEach
    void setup() {
        memberVo = MemberVo.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
    }

    @Test
    @DisplayName("멤버 저장 테스트")
    void save() {
        // given

        // when
        memberRepository.save(memberVo);

        // then
        assertThat(memberVo.getMemberId()).isNotNull();
    }

    @Test
    void findByMemberEmail() {
        // given
        memberRepository.save(memberVo);

        // when
        Optional<MemberVo> findMemberOptional = memberRepository.findByMemberEmail(memberVo.getMemberEmail());

        // then
        MemberVo findMemberVo = findMemberOptional.orElse(null);
        assertThat(findMemberVo).isNotNull();
        assertThat(findMemberVo.getMemberEmail()).isEqualTo("hello@naver.com");
    }

    @Test
    void  findByMemberNickname() {
        // given
        memberRepository.save(memberVo);

        // when
        Optional<MemberVo> findMemberOptional = memberRepository.findByMemberNickname(memberVo.getMemberNickname());

        // then
        MemberVo findMemberVo = findMemberOptional.orElse(null);
        assertThat(findMemberVo).isNotNull();
        assertThat(findMemberVo.getMemberNickname()).isEqualTo("한국");
    }

    @Test
    @DisplayName("멤버와 권한(List) Join 조회 테스트")
    void findMemberAndAuthByEmail() {
        // given
        memberRepository.save(memberVo);

        List<AuthorityVo> authorityVoList = new ArrayList<>();
        authorityVoList.add(AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build());
        authorityVoList.add(AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authorityRole(Role.ADMIN.getAuth())
                .build());
        authorityRepository.saveAuthorityList(authorityVoList);

        // when
        MemberAuthorityDto findMember = memberRepository.findMemberAndAuthByEmail(memberVo.getMemberEmail()).orElse(null);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(2);
        assertThat(findMember.getMemberEmail()).isEqualTo("hello@naver.com");
    }
}
