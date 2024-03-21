package nineto6.Talk.repository.authority;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.AuthorityVo;
import nineto6.Talk.repository.authority.AuthorityRepository;
import nineto6.Talk.repository.authority.dao.AuthorityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepository {
    private final AuthorityDao authorityDao;
    @Override
    public void saveAuthority(AuthorityVo authorityVo) {
        authorityDao.saveAuthority(authorityVo);
    }

    @Override
    public void saveAuthorityList(List<AuthorityVo> authorityVoList) {
        authorityDao.saveAuthorityList(authorityVoList);
    }
}
