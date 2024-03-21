package nineto6.Talk.repository.member;

import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.dto.member.MemberAuthorityDto;

import java.util.Optional;

public interface MemberRepository {
    void save(MemberVo memberVo);
    Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberId);
    Optional<MemberVo> findByMemberEmail(String memberEmail);
    Optional<MemberVo> findByMemberNickname(String memberNm);
}
