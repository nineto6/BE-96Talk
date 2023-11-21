package nineto6.Talk.repository.mapper;

import nineto6.Talk.domain.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    Optional<Profile> findByMemberNm(String memberNm);
    Optional<Profile> findByStoreFileName(String profileStoreFileName);
    void updateFileToNull(Long memberId);
    void updateStateMessageToNull(Long memberId);
}
