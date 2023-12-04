package nineto6.Talk.domain.profile.repository;

import nineto6.Talk.domain.profile.domain.ProfileMember;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.dto.ProfileSearchDto;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    void saveDefault(Profile profile);
    void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName);
    void updateStateMessageByMemberId(Long memberId, String profileStateMessage);
    Optional<Profile> findByMemberId(Long memberId);
    Optional<ProfileMember> findByMemberNickname(String memberNickname);
    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    List<ProfileMember> findFriendProfileListByMemberId(Long memberId);
    List<ProfileMember> findSearchProfileByKeyword(ProfileSearchDto search);
    Integer getMaxCount(ProfileSearchDto search);
}
