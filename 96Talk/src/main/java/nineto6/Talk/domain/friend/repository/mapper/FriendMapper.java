package nineto6.Talk.domain.friend.repository.mapper;

import nineto6.Talk.domain.friend.domain.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FriendMapper {
    void save(Friend friend);
    void deleteByMemberIdAndFriendMemberId(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId);
    List<Friend> findByMemberId(Long memberId);
    Optional<Friend> findByMemberIdAndFriendMemberId(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId);
}
