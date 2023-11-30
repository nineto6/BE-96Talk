package nineto6.Talk.repository;

import nineto6.Talk.domain.MemberProfile;
import nineto6.Talk.domain.Profile;
import nineto6.Talk.model.profile.ProfileSearchDto;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    void saveDefault(Profile profile);
    void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName);
    void updateStateMessageByMemberId(Long memberId, String profileStateMessage);
    Optional<Profile> findByMemberId(Long memberId);
    Optional<MemberProfile> findByMemberNickname(String memberNickname);
    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    List<MemberProfile> findFriendProfileListByMemberId(Long memberId);
    List<MemberProfile> findSearchProfileByKeyword(ProfileSearchDto search);
    Integer getMaxCount(ProfileSearchDto search);
}
