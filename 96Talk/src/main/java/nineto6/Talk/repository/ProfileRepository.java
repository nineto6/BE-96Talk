package nineto6.Talk.repository;

import nineto6.Talk.domain.Profile;

import java.util.Optional;

public interface ProfileRepository {
    void saveDefault(Profile profile);
    void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName);
    void updateStateMessageByMemberId(Long memberId, String profileStateMessage);
    Optional<Profile> findByMemberId(Long memberId);
}
