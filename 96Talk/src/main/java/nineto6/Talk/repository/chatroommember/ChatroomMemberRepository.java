package nineto6.Talk.repository.chatroommember;

import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.dto.chatroommember.ChatroomMemberDto;

import java.util.List;
import java.util.Optional;

public interface ChatroomMemberRepository {
    void save(ChatroomMemberVo chatroomMemberVo);
    void deleteByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<ChatroomMemberVo> findById(Long chatroomMemberId);
    Optional<ChatroomMemberVo> findByChatroomIdAndMemberId(Long chatroomId, Long memberId);
    Optional<ChatroomMemberVo> findByChannelIdAndMemberId(String channelId, Long memberId);
    void updateSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    void removeSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    void updateUnSubDateByChannelIdAndMemberId(String channelId, Long memberId);
    List<ChatroomMemberDto> findChatroomMemberDtoByMemberId(Long memberId);
    List<ChatroomMemberDto> findOtherChatroomMemberDtoByChannelIdAndNickname(String channelId, String nickname);
}
