package nineto6.Talk.repository.profile.dao;

import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.controller.profile.request.ProfileSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProfileDao {
    void saveDefault(ProfileVo profileVo);
    void updateFileByMemberId(@Param("memberId") Long memberId,
                              @Param("profileUploadFileName") String profileUploadFileName,
                              @Param("profileStoreFileName") String profileStoreFileName);
    void updateStateMessageByMemberId(@Param("memberId") Long memberId,
                                      @Param("profileStateMessage") String profileStateMessage);
    Optional<ProfileVo> findByMemberId(Long memberId);
    Optional<ProfileVo> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    Integer getMaxCount(ProfileSearchRequest search);
    Optional<ProfileMemberDto> findProfileMemberDtoByMemberNickname(String memberNickname);
    List<ProfileMemberDto> findProfileMemberDtoByMemberId(Long memberId);
    List<ProfileMemberDto> findProfileMemberDtoByProfileSearchRequest(ProfileSearchRequest search);
}
