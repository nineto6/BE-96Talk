package nineto6.Talk.repository;

import nineto6.Talk.domain.Authority;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    void saveAuthority(Authority authority);
    void saveAuthorityList(List<Authority> authorityList);
    Optional<MemberAuthority> findMemberAndAuthByEmail(String memberId);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNm(String memberNm);
}
