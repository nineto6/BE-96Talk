package nineto6.Talk.domain.member.authority.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.member.authority.domain.Authority;
import nineto6.Talk.domain.member.authority.repository.AuthorityRepository;
import nineto6.Talk.domain.member.authority.repository.mapper.AuthorityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepository {
    private final AuthorityMapper authorityMapper;
    @Override
    public void saveAuthority(Authority authority) {
        authorityMapper.saveAuthority(authority);
    }

    @Override
    public void saveAuthorityList(List<Authority> authorityList) {
        authorityMapper.saveAuthorityList(authorityList);
    }
}
