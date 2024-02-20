package nineto6.Talk.domain.member.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.dto.MemberAuthorityDto;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.member.repository.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberMapper memberMapper;
    @Override
    public void save(Member member) {
        memberMapper.save(member);
    }

    @Override
    public Optional<MemberAuthorityDto> findMemberAndAuthByEmail(String memberId) {
        return memberMapper.findMemberAndAuthByEmail(memberId);
    }

    @Override
    public Optional<Member> findByMemberEmail(String memberEmail) {
        return memberMapper.findByMemberEmail(memberEmail);
    }

    @Override
    public Optional<Member> findByMemberNickname(String memberNm) {
        return memberMapper.findByMemberNickname(memberNm);
    }
}
