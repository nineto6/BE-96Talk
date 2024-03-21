package nineto6.Talk.repository.friend;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.FriendVo;
import nineto6.Talk.repository.friend.dao.FriendDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final FriendDao friendDao;
    @Override
    public void save(FriendVo friendVo) {
        friendDao.save(friendVo);
    }

    @Override
    public void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId) {
        friendDao.deleteByMemberIdAndFriendMemberId(memberId, friendMemberId);
    }

    @Override
    public List<FriendVo> findByMemberId(Long memberId) {
        return friendDao.findByMemberId(memberId);
    }

    @Override
    public Optional<FriendVo> findByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId) {
        return friendDao.findByMemberIdAndFriendMemberId(memberId, friendMemberId);
    }
}
