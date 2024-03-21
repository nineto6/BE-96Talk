package nineto6.Talk.repository.friend.dao;

import nineto6.Talk.vo.FriendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FriendDao {
    void save(FriendVo friendVo);
    void deleteByMemberIdAndFriendMemberId(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId);
    List<FriendVo> findByMemberId(Long memberId);
    Optional<FriendVo> findByMemberIdAndFriendMemberId(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId);
}
