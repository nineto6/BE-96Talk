package nineto6.Talk.repository.profile;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.controller.profile.request.ProfileSearchRequest;
import nineto6.Talk.repository.profile.dao.ProfileDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {
    private final ProfileDao profileDao;

    @Override
    public void saveDefault(ProfileVo profileVo) {
        profileDao.saveDefault(profileVo);
    }

    @Override
    public void updateFileByMemberId(Long memberId, String profileUploadFileName, String profileStoreFileName) {
        profileDao.updateFileByMemberId(memberId, profileUploadFileName, profileStoreFileName);
    }

    @Override
    public void updateStateMessageByMemberId(Long memberId, String profileStateMessage) {
        profileDao.updateStateMessageByMemberId(memberId, profileStateMessage);
    }

    @Override
    public Optional<ProfileVo> findByMemberId(Long memberId) {
        return profileDao.findByMemberId(memberId);
    }

    @Override
    public Optional<ProfileMemberDto> findProfileMemberDtoByMemberNickname(String memberNickname) {
        return profileDao.findProfileMemberDtoByMemberNickname(memberNickname);
    }

    @Override
    public Optional<ProfileVo> findByStoreFileName(String profileStoreFileName) {
        return profileDao.findByStoreFileName(profileStoreFileName);
    }

    @Override
    public void updateFileToNull(Long memberId) {
        profileDao.updateFileToNull(memberId);
    }

    @Override
    public void updateStateMessageToNull(Long memberId) {
        profileDao.updateStateMessageToNull(memberId);
    }

    @Override
    public List<ProfileMemberDto> findProfileMemberDtoByMemberId(Long memberId) {
        return profileDao.findProfileMemberDtoByMemberId(memberId);
    }

    @Override
    public List<ProfileMemberDto> findProfileMemberDtoByProfileSearchRequest(ProfileSearchRequest search) {
        return profileDao.findProfileMemberDtoByProfileSearchRequest(search);
    }

    @Override
    public Integer getMaxCount(ProfileSearchRequest search) {
        return profileDao.getMaxCount(search);
    }
}
