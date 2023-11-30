package nineto6.Talk.repository.impl;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.MemberProfile;
import nineto6.Talk.domain.Profile;
import nineto6.Talk.model.profile.ProfileSearchDto;
import nineto6.Talk.repository.ProfileRepository;
import nineto6.Talk.repository.mapper.ProfileMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Optional<MemberProfile> findByMemberNickname(String memberNickname) {
        return profileMapper.findByMemberNickname(memberNickname);
    }

    @Override
    public Optional<Profile> findByStoreFileName(String profileStoreFileName) {
        return profileMapper.findByStoreFileName(profileStoreFileName);
    }

    @Override
    public void updateFileToNull(Long memberId) {
        profileMapper.updateFileToNull(memberId);
    }

    @Override
    public void updateStateMessageToNull(Long memberId) {
        profileMapper.updateStateMessageToNull(memberId);
    }

    @Override
    public List<MemberProfile> findFriendProfileListByMemberId(Long memberId) {
        return profileMapper.findFriendProfileListByMemberId(memberId);
    }

    @Override
    public List<MemberProfile> findSearchProfileByKeyword(ProfileSearchDto search) {
        return profileMapper.findSearchProfileByKeyword(search);
    }

    @Override
    public Integer getMaxCount(ProfileSearchDto search) {
        return profileMapper.getMaxCount(search);
    }
}
