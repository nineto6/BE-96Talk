package nineto6.Talk.domain.friend.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.friend.domain.Friend;
import nineto6.Talk.domain.friend.repository.FriendRepository;
import nineto6.Talk.domain.friend.repository.mapper.FriendMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final FriendMapper friendMapper;
    @Override
    public void save(Friend friend) {
        friendMapper.save(friend);
    }

    @Override
    public void deleteByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId) {
        friendMapper.deleteByMemberIdAndFriendMemberId(memberId, friendMemberId);
    }

    @Override
    public List<Friend> findByMemberId(Long memberId) {
        return friendMapper.findByMemberId(memberId);
    }

    @Override
    public Optional<Friend> findByMemberIdAndFriendMemberId(Long memberId, Long friendMemberId) {
        return friendMapper.findByMemberIdAndFriendMemberId(memberId, friendMemberId);
    }
}
