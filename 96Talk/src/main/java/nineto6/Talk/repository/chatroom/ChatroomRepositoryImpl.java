package nineto6.Talk.repository.chatroom;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.dto.chatroom.ChatroomProfileDto;
import nineto6.Talk.repository.chatroom.dao.ChatroomDao;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepository {
    private final ChatroomDao chatroomDao;
    @Override
    public void save(ChatroomVo chatroomVo) {
        chatroomDao.save(chatroomVo);
    }

    @Override
    public Optional<ChatroomVo> findById(Long chatroomId) {
        return chatroomDao.findById(chatroomId);
    }

    @Override
    public Optional<ChatroomVo> findByChannelId(String chatroomChannelId) {
        return chatroomDao.findByChannelId(chatroomChannelId);
    }

    @Override
    public void deleteByChannelId(String chatroomChannelId) {
        chatroomDao.deleteByChannelId(chatroomChannelId);
    }

    @Override
    public List<ChatroomProfileDto> findChannelIdAndMemberProfileListByMemberId(Long memberId) {
        return chatroomDao.findChannelIdAndMemberProfileListByMemberId(memberId);
    }

    @Override
    public List<ProfileMemberDto> findMemberProfileByChatroomChannelId(String chatroomChannelId){
        return chatroomDao.findMemberProfileByChatroomChannelId(chatroomChannelId);
    }

    @Override
    public Optional<ChatroomVo> findChatroomByMemberIdAndFriendId(Long memberId, Long friendId) {
        return chatroomDao.findChatroomByMemberIdAndFriendId(memberId, friendId);
    }

    @Override
    public Optional<ChatroomVo> findChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId) {
        return chatroomDao.findChatroomByChannelIdAndMemberId(chatroomChannelId, memberId);
    }

    @Override
    public List<String> findNotFriendInChatroomByChannelIdAndMemberId(String chatroomChannelId, Long memberId) {
        return chatroomDao.findNotFriendInChatroomByChannelIdAndMemberId(chatroomChannelId, memberId);
    }
}
