package nineto6.Talk.domain.profile.repository;

import nineto6.Talk.domain.profile.dto.ProfileMemberDto;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.controller.request.ProfileSearchDto;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    void saveDefault(Profile profile);
    void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName);
    void updateStateMessageByMemberId(Long memberId, String profileStateMessage);
    Optional<Profile> findByMemberId(Long memberId);
    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    Integer getMaxCount(ProfileSearchDto search);
    Optional<ProfileMemberDto> findProfileMemberDtoByMemberNickname(String memberNickname);
    List<ProfileMemberDto> findProfileMemberDtoByMemberId(Long memberId);
    List<ProfileMemberDto> findProfileMemberDtoByProfileSearchDto(ProfileSearchDto search);
}
