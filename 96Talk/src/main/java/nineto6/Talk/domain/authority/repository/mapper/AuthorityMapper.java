package nineto6.Talk.domain.authority.repository.mapper;

import nineto6.Talk.domain.authority.domain.Authority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorityMapper {
    void saveAuthority(Authority authority);
    void saveAuthorityList(List<Authority> authorityList);
}
