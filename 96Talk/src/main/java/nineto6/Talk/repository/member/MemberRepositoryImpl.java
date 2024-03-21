package nineto6.Talk.repository.member;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.dto.member.MemberAuthorityDto;
import nineto6.Talk.repository.member.dao.MemberDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberDao memberDao;
    @Override
    public void save(MemberVo memberVo) {
        memberDao.save(memberVo);
    }

    @Override
    public Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberId) {
        return memberDao.findMemberAndAuthByEmail(memberId);
    }

    @Override
    public Optional<MemberVo> findByMemberEmail(String memberEmail) {
        return memberDao.findByMemberEmail(memberEmail);
    }

    @Override
    public Optional<MemberVo> findByMemberNickname(String memberNm) {
        return memberDao.findByMemberNickname(memberNm);
    }
}
