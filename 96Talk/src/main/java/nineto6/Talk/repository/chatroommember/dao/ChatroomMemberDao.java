package nineto6.Talk.repository.chatroommember.dao;

import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.dto.chatroommember.ChatroomMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatroomMemberDao {
    void save(ChatroomMemberVo chatroomMemberVo);
    void deleteByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);
    Optional<ChatroomMemberVo> findById(Long chatroomMemberId);
    Optional<ChatroomMemberVo> findByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);
    Optional<ChatroomMemberVo> findByChannelIdAndMemberId(@Param("channelId")String channelId, @Param("memberId") Long memberId);
    void updateSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    void removeSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    void updateUnSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    List<ChatroomMemberDto> findChatroomMemberDtoByMemberId(Long memberId);
    List<ChatroomMemberDto> findOtherChatroomMemberDtoByChannelIdAndNickname(@Param("channelId") String channelId, @Param("nickname") String nickname);
}
