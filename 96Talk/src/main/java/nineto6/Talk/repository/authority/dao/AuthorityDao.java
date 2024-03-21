package nineto6.Talk.repository.authority.dao;

import nineto6.Talk.vo.AuthorityVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorityDao {
    void saveAuthority(AuthorityVo authorityVo);
    void saveAuthorityList(List<AuthorityVo> authorityVoList);
}
