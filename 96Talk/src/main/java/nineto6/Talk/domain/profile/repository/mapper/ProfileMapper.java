package nineto6.Talk.domain.profile.repository.mapper;

import nineto6.Talk.domain.profile.dto.ProfileMemberDto;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.controller.request.ProfileSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProfileMapper {
    void saveDefault(Profile profile);
    void updateFileByMemberId(@Param("memberId") Long memberId,
                              @Param("profileUploadFileName") String profileUploadFileName,
                              @Param("profileStoreFileName") String profileStoreFileName);
    void updateStateMessageByMemberId(@Param("memberId") Long memberId,
                                      @Param("profileStateMessage") String profileStateMessage);
    Optional<Profile> findByMemberId(Long memberId);
    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    Integer getMaxCount(ProfileSearchDto search);
    Optional<ProfileMemberDto> findProfileMemberDtoByMemberNickname(String memberNickname);
    List<ProfileMemberDto> findProfileMemberDtoByMemberId(Long memberId);
    List<ProfileMemberDto> findProfileMemberDtoByProfileSearchDto(ProfileSearchDto search);
}
