package nineto6.Talk.repository.authority;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.dto.authority.code.Role;
import nineto6.Talk.vo.AuthorityVo;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.dto.member.MemberAuthorityDto;
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

    private static MemberVo memberVo;
    private static AuthorityVo authorityVo;
    @BeforeEach
    void setup() {
        memberVo = MemberVo.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();

        memberRepository.save(memberVo);

        authorityVo = AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build();
    }

    @Test
    @DisplayName("권한 1개 저장 테스트")
    void saveAuthorityList_1() {
        // given

        // when
        authorityRepository.saveAuthority(authorityVo);

        // then
        MemberAuthorityDto findMember = memberRepository.findMemberAndAuthByEmail(memberVo.getMemberEmail()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("권한 2개 저장 테스트")
    void saveAuthorityList_2() {
        // given
        List<AuthorityVo> authorityVoList = new ArrayList<>();
        authorityVoList.add(AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build());
        authorityVoList.add(AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authorityRole(Role.ADMIN.getAuth())
                .build());

        // when
        authorityRepository.saveAuthorityList(authorityVoList);

        // then
        MemberAuthorityDto findMember = memberRepository.findMemberAndAuthByEmail(memberVo.getMemberEmail()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getRoleList().size()).isEqualTo(2);
    }

}
