package nineto6.Talk.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
import nineto6.Talk.repository.MemberRepository;
import nineto6.Talk.repository.mapper.MemberMapper;
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
    public Optional<MemberAuthority> findMemberAndAuthByEmail(String memberId) {
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
