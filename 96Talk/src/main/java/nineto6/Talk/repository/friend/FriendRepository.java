package nineto6.Talk.repository.friend;

import nineto6.Talk.vo.FriendVo;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    void save(FriendVo friendVo);
    void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
    List<FriendVo> findByMemberId(Long memberId);
    Optional<FriendVo> findByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
}
