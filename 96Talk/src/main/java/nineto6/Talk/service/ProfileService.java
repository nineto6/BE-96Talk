package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.global.common.file.FileService;
import nineto6.Talk.global.common.file.UploadFile;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.ResourceExceptionHandler;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.global.common.pagination.Pagination;
import nineto6.Talk.global.common.pagination.PagingResponseDto;
import nineto6.Talk.dto.member.MemberDto;
import nineto6.Talk.controller.profile.response.ProfileResponse;
import nineto6.Talk.controller.profile.request.ProfileSearchRequest;
import nineto6.Talk.repository.profile.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FileService fileService;

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
        if(!fileService.isImageFile(uploadFile)) {
            log.info("파일 확장자가 jpg, png 가 아닙니다.");
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }

        // Profile 값을 DB 에서 조회
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberDto.getMemberId());

        // 파일 저장 및 DB 업데이트
        try {
            UploadFile file = fileService.storeFile(uploadFile);
            profileRepository.updateFileByMemberId(memberDto.getMemberId(), file.getUploadFileName(), file.getStoreFileName());
        } catch(IOException e) {
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }

        // Profile DB 에 파일이 존재할 경우
        if(profileOptional.isPresent()) {
            ProfileVo profileVo = profileOptional.get();
            // storeFileName 과 uploadFileName 이 존재할 경우
            if(!ObjectUtils.isEmpty(profileVo.getProfileStoreFileName()) && !ObjectUtils.isEmpty(profileVo.getProfileUploadFileName())) {
                if(!fileService.removeFile(profileVo)) {
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
     * 프로필 이미지의 값을 제거
     */
    @Transactional
    public void updateImageToNull(MemberDto memberDto) {
        // 조회된 값이 없으면 Exception
        ProfileVo profileVo = profileRepository.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR));

        // profile 내부에 이미지 관련 값들이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(profileVo.getProfileStoreFileName()) || ObjectUtils.isEmpty(profileVo.getProfileStoreFileName())) {
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }

        // 이미지 파일 관련 값들을 NULL 로 업데이트
        profileRepository.updateFileToNull(memberDto.getMemberId());

        // 저장소에서 이미지 파일 삭제
        if(!fileService.removeFile(profileVo)) {
            log.info("파일 삭제 실패");
            throw new BusinessExceptionHandler(ErrorCode.FILE_EXCEPTION_ERROR);
        }
    }

    /**
     * 프로필 상태 메세지 제거
     */
    @Transactional
    public void updateStateMessageToNull(MemberDto memberDto) {
        // 조회된 값이 없으면 Exception
        ProfileVo profileVo = profileRepository.findByMemberId(memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR));

        // profile 내부에 상태메세지 값이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(profileVo.getProfileStateMessage())) {
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }

        // 상태메세지 NULL 로 업데이트
        profileRepository.updateStateMessageToNull(memberDto.getMemberId());
    }

    /**
     * 프로필 검색 조회
     */
    public PagingResponseDto<ProfileResponse> getSearchProfile(ProfileSearchRequest params) {
        Integer count = profileRepository.getMaxCount(params);

        if (count < 1) {
            // 조건에 해달하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null 을 담아 반호나
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params 에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<ProfileMemberDto> findProfileMemberDto = profileRepository.findProfileMemberDtoByProfileSearchRequest(params);

        List<ProfileResponse> profileResponseList = findProfileMemberDto.stream()
                .map(this::getProfileResponse)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(profileResponseList, pagination);
    }

    /**
     * 친구 프로필 전체 조회
     */
    public List<ProfileResponse> findFriendProfiles(MemberDto memberDto) {
        List<ProfileMemberDto> friendProfileList = profileRepository.findProfileMemberDtoByMemberId(memberDto.getMemberId());

        // 친구 프로필 조회
        return friendProfileList.stream()
                .map(this::getProfileResponse)
                .collect(Collectors.toList());
    }

    /**
     * MemberProfile -> ProfileResponse 변환 메서드
     */
    public ProfileResponse getProfileResponse(ProfileMemberDto profileMemberDto) {
        ProfileVo profileVo = profileMemberDto.getProfileVo();
        if (ObjectUtils.isEmpty(profileVo.getProfileStoreFileName()) || ObjectUtils.isEmpty(profileVo.getProfileUploadFileName())) {
            return ProfileResponse.builder()
                    .memberNickname(profileMemberDto.getMemberNickname())
                    .profileStateMessage(profileVo.getProfileStateMessage())
                    .build();
        }

        String storeName = profileVo.getProfileStoreFileName();
        String[] split = storeName.split("\\.");
        String uuid = split[0];
        String ext = split[1];

        return ProfileResponse.builder()
                .memberNickname(profileMemberDto.getMemberNickname())
                .profileStateMessage(profileVo.getProfileStateMessage())
                .imageName(uuid)
                .type(fileService.getTypeByExt(ext))
                .build();
    }

    /**
     * 프로필 조회
     */
    public ProfileResponse findByNickname(String memberNm) {
        ProfileMemberDto profileMemberDto = profileRepository.findProfileMemberDtoByMemberNickname(memberNm)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        // 프로필이 존재하지 않을 경우 Exception
        if(ObjectUtils.isEmpty(profileMemberDto.getProfileVo())) {
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        return getProfileResponse(profileMemberDto);
    }

    /**
     * 저장소에 저장된 이미지 파일이 프로필에 존재하는지 확인
     */
    public void checkByStoreFileName(String profileStoreFileName) {
        ProfileVo profileVo = profileRepository.findByStoreFileName(profileStoreFileName)
                .orElseThrow(() -> new ResourceExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        if(ObjectUtils.isEmpty(profileVo.getProfileStoreFileName()) || ObjectUtils.isEmpty(profileVo.getProfileUploadFileName())) {
            throw new ResourceExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }
}
