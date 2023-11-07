package nineto6.Talk.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.Authority;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
import nineto6.Talk.repository.MemberRepository;
import nineto6.Talk.repository.mapper.AuthorityMapper;
import nineto6.Talk.repository.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberMapper memberMapper;
    private final AuthorityMapper authorityMapper;

    @Override
    public void save(Member member) {
        memberMapper.save(member);
    }

    @Override
    public void saveAuthority(Authority authority) {
        authorityMapper.saveAuthority(authority);
    }

    @Override
    public void saveAuthorityList(List<Authority> authorityList) {
        authorityMapper.saveAuthorityList(authorityList);
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
    public Optional<Member> findByMemberNm(String memberNm) {
        return memberMapper.findByMemberNm(memberNm);
    }
}
