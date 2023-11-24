package nineto6.Talk.repository;

import nineto6.Talk.domain.Friend;
import java.util.List;

public interface FriendRepository {
    void save(Friend friend);
    void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId);
    List<Friend> findByMemberId(Long memberId);
}
