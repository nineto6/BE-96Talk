package nineto6.Talk.repository.authority;

import nineto6.Talk.vo.AuthorityVo;

import java.util.List;

public interface AuthorityRepository {
    void saveAuthority(AuthorityVo authorityVo);
    void saveAuthorityList(List<AuthorityVo> authorityVoList);
}
