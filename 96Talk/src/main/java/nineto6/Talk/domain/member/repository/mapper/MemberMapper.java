package nineto6.Talk.domain.member.repository.mapper;

import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.dto.MemberAuthorityDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNickname(String memberNickname);
    Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberEmail);
}
