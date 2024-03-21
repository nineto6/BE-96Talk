package nineto6.Talk.repository.member.dao;

import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.dto.member.MemberAuthorityDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberDao {
    void save(MemberVo memberVo);
    Optional<MemberVo> findByMemberEmail(String memberEmail);
    Optional<MemberVo> findByMemberNickname(String memberNickname);
    Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberEmail);
}
