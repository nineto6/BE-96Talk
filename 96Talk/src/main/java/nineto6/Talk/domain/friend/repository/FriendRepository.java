package nineto6.Talk.domain.friend.repository;

import nineto6.Talk.domain.friend.domain.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    void save(Friend friend);
    void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
    List<Friend> findByMemberId(Long memberId);
    Optional<Friend> findByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
}
