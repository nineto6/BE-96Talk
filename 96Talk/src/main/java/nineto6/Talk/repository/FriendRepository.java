package nineto6.Talk.repository;

import nineto6.Talk.domain.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    void save(Friend friend);
    void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
    List<Friend> findByMemberId(Long memberId);
    Optional<Friend> findByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
}
