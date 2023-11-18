package nineto6.Talk.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.Profile;
import nineto6.Talk.repository.ProfileRepository;
import nineto6.Talk.repository.mapper.ProfileMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {
    private final ProfileMapper profileMapper;

    @Override
    public void saveDefault(Profile profile) {
        profileMapper.saveDefault(profile);
    }

    @Override
    public void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName) {
        profileMapper.updateFileByMemberId(memberId, profileUploadFileName, profileStoreFileName);
    }

    @Override
    public void updateStateMessageByMemberId(Long memberId, String profileStateMessage) {
        profileMapper.updateStateMessageByMemberId(memberId, profileStateMessage);
    }

    @Override
    public Optional<Profile> findByMemberId(Long memberId) {
        return profileMapper.findByMemberId(memberId);
    }
}
