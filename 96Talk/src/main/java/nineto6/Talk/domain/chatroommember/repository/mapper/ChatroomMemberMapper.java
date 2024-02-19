package nineto6.Talk.domain.chatroommember.repository.mapper;

import nineto6.Talk.domain.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndChannelId;
import nineto6.Talk.domain.chatroommember.domain.ChatroomMemberAndNickname;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatroomMemberMapper {
    void save(ChatroomMember chatroomMember);
    void deleteByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);
    Optional<ChatroomMember> findById(Long chatroomMemberId);
    Optional<ChatroomMember> findByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);
    Optional<ChatroomMember> findByChannelIdAndMemberId(@Param("channelId")String channelId, @Param("memberId") Long memberId);
    List<ChatroomMemberAndChannelId> findByMemberId(Long memberId);
    void updateSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    void removeSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    void updateUnSubDateByChannelIdAndMemberId(@Param("channelId") String channelId, @Param("memberId") Long memberId);
    List<ChatroomMemberAndNickname> findOtherUserListByChannelIdAndNickname(@Param("channelId") String channelId, @Param("nickname") String nickname);
}
