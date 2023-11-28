package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.common.exception.ResourceExceptionHandler;
import nineto6.Talk.domain.MemberProfile;
import nineto6.Talk.domain.Profile;
import nineto6.Talk.model.UploadFile;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.model.profile.ProfileResponse;
import nineto6.Talk.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FileStore fileStore;

    /**
     * 친구 프로필 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> findFriendProfiles(MemberDto memberDto) {
        List<MemberProfile> friendProfileList = profileRepository.findFriendProfileListByMemberId(memberDto.getMemberId());

        // 친구 프로필 조회
        return friendProfileList.stream()
                .map((memberProfile) -> {
                            Profile profile = memberProfile.getProfile();
                            if (ObjectUtils.isEmpty(profile.getProfileStoreFileName()) || ObjectUtils.isEmpty(profile.getProfileUploadFileName())) {
                                return ProfileResponse.builder()
                                        .memberNm(memberProfile.getMemberNm())
                                        .profileStateMessage(profile.getProfileStateMessage())
                                        .build();
                            }

                            String storeName = profile.getProfileStoreFileName();
                            String[] split = storeName.split("\\.");
                            String uuid = split[0];
                            String ext = split[1];

                            return ProfileResponse.builder()
                                    .memberNm(memberProfile.getMemberNm())
                                    .profileStateMessage(profile.getProfileStateMessage())
                                    .imageName(uuid)
                                    .type(fileStore.getTypeByExt(ext))
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

    /**
     * 프로필 조회
     */
    @Transactional(readOnly = true)
    public ProfileResponse findByNickname(String memberNm) {
        MemberProfile memberProfile = profileRepository.findByMemberNm(memberNm)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        // 프로필이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(memberProfile.getProfile())) {
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        Profile profile = memberProfile.getProfile();
        // 이미지 파일이 존재하지 않을 경우
        if(ObjectUtils.isEmpty(profile.getProfileStoreFileName()) || ObjectUtils.isEmpty(profile.getProfileUploadFileName())) {
            return ProfileResponse.builder()
                    .memberNm(memberProfile.getMemberNm())
                    .profileStateMessage(profile.getProfileStateMessage())
                    .build();
        }

        String storeName = profile.getProfileStoreFileName();
        String[] split = storeName.split("\\.");
        String uuid = split[0];
        String ext = split[1];

        return ProfileResponse.builder()
                .memberNm(memberProfile.getMemberNm())
                .profileStateMessage(profile.getProfileStateMessage())
                .imageName(uuid)
                .type(fileStore.getTypeByExt(ext))
                .build();
    }

    /**
     * 프로필 이미지 수정
     */
    @Transactional
    public void updateImageFile(MemberDto memberDto, MultipartFile uploadFile) {
        // 업로드 파일 안에 값이 비어있을 경우
        if(uploadFile.isEmpty()) {
            log.info("파일 안에 값이 없습니다.");
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }

        // 파일이 이미지 파일이 아닐 경우(jpg, png)
        if(!fileStore.isImageFile(uploadFile)) {
            log.info("파일 확장자가 jpg, png 가 아닙니다.");
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }

        // Profile 값을 DB 에서 조회
        Optional<Profile> profileOptional = profileRepository.findByMemberId(memberDto.getMemberId());

        // 파일 저장 및 DB 업데이트
        try {
            UploadFile file = fileStore.storeFile(uploadFile);
            profileRepository.updateFileByMemberId(memberDto.getMemberId(), file.getUploadFileName(), file.getStoreFileName());
        } catch(IOException e) {
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }

        // Profile DB 에 파일이 존재할 경우
        if(profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            // storeFileName 과 uploadFileName 이 존재할 경우
            if(!ObjectUtils.isEmpty(profile.getProfileStoreFileName()) && !ObjectUtils.isEmpty(profile.getProfileUploadFileName())) {
                if(!fileStore.removeFile(profile)) {
                    log.info("파일 삭제 실패");
                    throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
                }
            }
        }
    }

    /**
     *  프로필 상태 메세지 수정
     */
    @Transactional
    public void updateStateMessage(MemberDto memberDto, String stateMessage) {
        // 상태 메세지 업데이트
        profileRepository.updateStateMessageByMemberId(memberDto.getMemberId(), stateMessage);
    }

    /**
     * 저장소에 저장된 이미지 파일이 프로필에 존재하는지 확인
     */
    @Transactional(readOnly = true)
    public void checkByStoreFileName(String profileStoreFileName) {
        Profile profile = profileRepository.findByStoreFileName(profileStoreFileName)
                .orElseThrow(() -> new ResourceExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        if(ObjectUtils.isEmpty(profile.getProfileStoreFileName()) || ObjectUtils.isEmpty(profile.getProfileUploadFileName())) {
            throw new ResourceExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }

    /**
     * 프로필 이미지의 값을 제거
     */

    public void updateImageToNull(MemberDto memberDto) {
        // 조회된 값이 없으면 Exception
        Profile profile = profileRepository.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR));

        // profile 내부에 이미지 관련 값들이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(profile.getProfileStoreFileName()) || ObjectUtils.isEmpty(profile.getProfileStoreFileName())) {
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }

        // 이미지 파일 관련 값들을 NULL 로 업데이트
        profileRepository.updateFileToNull(memberDto.getMemberId());

        // 저장소에서 이미지 파일 삭제
        if(!fileStore.removeFile(profile)) {
            log.info("파일 삭제 실패");
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }
    }

    /**
     * 프로필 상태 메세지 제거
     */
    public void updateStateMessageToNull(MemberDto memberDto) {
        // 조회된 값이 없으면 Exception
        Profile profile = profileRepository.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR));

        // profile 내부에 상태메세지 값이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(profile.getProfileStateMessage())) {
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }

        // 상태메세지 NULL 로 업데이트
        profileRepository.updateStateMessageToNull(memberDto.getMemberId());
    }
}
