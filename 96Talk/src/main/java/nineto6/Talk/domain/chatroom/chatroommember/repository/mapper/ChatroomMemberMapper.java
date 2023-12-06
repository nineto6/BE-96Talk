package nineto6.Talk.domain.chatroom.chatroommember.repository.mapper;

import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ChatroomMemberMapper {
    void save(ChatroomMember chatroomMember);
    void deleteByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);
    Optional<ChatroomMember> findById(Long chatroomMemberId);
}
