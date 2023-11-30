package nineto6.Talk.repository;

import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<MemberAuthority> findMemberAndAuthByEmail(String memberId);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNickname(String memberNm);
}
