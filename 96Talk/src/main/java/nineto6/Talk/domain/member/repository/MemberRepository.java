package nineto6.Talk.domain.member.repository;

import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.dto.MemberAuthorityDto;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberId);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNickname(String memberNm);
}
