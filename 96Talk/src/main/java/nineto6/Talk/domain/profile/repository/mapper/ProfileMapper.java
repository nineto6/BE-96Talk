package nineto6.Talk.domain.profile.repository.mapper;

import nineto6.Talk.domain.profile.domain.ProfileMember;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.profile.dto.ProfileSearchDto;
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
    Optional<ProfileMember> findByMemberNickname(String memberNickname);

    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
    List<ProfileMember> findFriendProfileListByMemberId(Long memberId);
    List<ProfileMember> findSearchProfileByKeyword(ProfileSearchDto search);
    Integer getMaxCount(ProfileSearchDto search);
}
