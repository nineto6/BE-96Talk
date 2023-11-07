package nineto6.Talk.repository.mapper;

import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNm(String memberNm);
    Optional<MemberAuthority> findMemberAndAuthByEmail(String memberEmail);
}
