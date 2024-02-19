package nineto6.Talk.domain.authority.repository;

import nineto6.Talk.domain.authority.domain.Authority;

import java.util.List;

public interface AuthorityRepository {
    void saveAuthority(Authority authority);
    void saveAuthorityList(List<Authority> authorityList);
}
