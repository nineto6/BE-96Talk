package nineto6.Talk.repository.profile;

import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.controller.profile.request.ProfileSearchRequest;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    void saveDefault(ProfileVo profileVo);
    void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName);
    void updateStateMessageByMemberId(Long memberId, String profileStateMessage);
    Optional<ProfileVo> findByMemberId(Long memberId);
    Optional<ProfileVo> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    Integer getMaxCount(ProfileSearchRequest search);
    Optional<ProfileMemberDto> findProfileMemberDtoByMemberNickname(String memberNickname);
    List<ProfileMemberDto> findProfileMemberDtoByMemberId(Long memberId);
    List<ProfileMemberDto> findProfileMemberDtoByProfileSearchRequest(ProfileSearchRequest search);
}
