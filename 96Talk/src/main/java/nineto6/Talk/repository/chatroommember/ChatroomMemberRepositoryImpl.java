package nineto6.Talk.repository.chatroommember;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.dto.chatroommember.ChatroomMemberDto;
import nineto6.Talk.repository.chatroommember.dao.ChatroomMemberDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomMemberRepositoryImpl implements ChatroomMemberRepository {
    private final ChatroomMemberDao chatroomMemberDao;
    @Override
    public void save(ChatroomMemberVo chatroomMemberVo) {
        chatroomMemberDao.save(chatroomMemberVo);
    }

    @Override
    public void deleteByChatroomIdAndMemberId(Long chatroomId, Long memberId) {
        chatroomMemberDao.deleteByChatroomIdAndMemberId(chatroomId, memberId);
    }

    @Override
    public Optional<ChatroomMemberVo> findById(Long chatroomMemberId) {
        return chatroomMemberDao.findById(chatroomMemberId);
    }

    @Override
    public Optional<ChatroomMemberVo> findByChatroomIdAndMemberId(Long chatroomId, Long memberId) {
        return chatroomMemberDao.findByChatroomIdAndMemberId(chatroomId, memberId);
    }

    @Override
    public Optional<ChatroomMemberVo> findByChannelIdAndMemberId(String channelId, Long memberId) {
        return chatroomMemberDao.findByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public void updateSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberDao.updateSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public void removeSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberDao.removeSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public void updateUnSubDateByChannelIdAndMemberId(String channelId, Long memberId) {
        chatroomMemberDao.updateUnSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    @Override
    public List<ChatroomMemberDto> findChatroomMemberDtoByMemberId(Long memberId) {
        return chatroomMemberDao.findChatroomMemberDtoByMemberId(memberId);
    }

    @Override
    public List<ChatroomMemberDto> findOtherChatroomMemberDtoByChannelIdAndNickname(String channelId, String nickname) {
        return chatroomMemberDao.findOtherChatroomMemberDtoByChannelIdAndNickname(channelId, nickname);
    }
}
