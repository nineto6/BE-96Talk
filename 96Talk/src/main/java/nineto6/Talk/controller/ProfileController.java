package nineto6.Talk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.ImageCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.ResourceExceptionHandler;
import nineto6.Talk.controller.swagger.ProfileControllerDocs;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.profile.ProfileResponse;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.FileStore;
import nineto6.Talk.service.ProfileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerDocs {
    private final ProfileService profileService;
    private final FileStore fileStore;

    /**
     * 자기 자신 프로필 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getProfile(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        ProfileResponse profileResponse = profileService.findByNickname(memberDetailsDto.getMemberDto().getMemberNm());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(profileResponse)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/{memberNm}")
    public ResponseEntity<ApiResponse> getProfileByMemberNm(@PathVariable("memberNm") String memberNm) {
        ProfileResponse profileResponse = profileService.findByNickname(memberNm);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(profileResponse)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }


    /**
     * 프로필 상태 메세지 삭제
     */
    @DeleteMapping()
    public ResponseEntity<ApiResponse> deleteProfileStateMessage(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        profileService.updateStateMessageToNull(memberDetailsDto.getMemberDto());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.DELETE_SUCCESS.getStatus())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.DELETE_SUCCESS.getHttpStatus());
    }

    /**
     * 프로필 이미지, 상태 메세지 수정
     */
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> updateProfileImageFileAndStateMessage(@RequestPart(required = false) MultipartFile imageFile,
                                                              @RequestPart(required = false) String profileStateMessage,
                                                              @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        if(!ObjectUtils.isEmpty(imageFile)) {
            profileService.updateImageFile(memberDetailsDto.getMemberDto(), imageFile);
        }

        if(!ObjectUtils.isEmpty(profileStateMessage)) {
            profileService.updateStateMessage(memberDetailsDto.getMemberDto(), profileStateMessage);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.UPDATE_SUCCESS.getStatus())
                .message(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.UPDATE_SUCCESS.getHttpStatus());
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/images")
    public ResponseEntity<ApiResponse> deleteProfileImage(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        profileService.updateImageToNull(memberDetailsDto.getMemberDto());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.DELETE_SUCCESS.getStatus())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.DELETE_SUCCESS.getHttpStatus());
    }

    /**
     *  프로필 이미지 다운로드
     */
    @GetMapping(value = "/images/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> downloadImage(@PathVariable("imageName") String imageName,
                                  HttpServletRequest request) throws IOException {
        String type = getFileTypeByRequest(request);

        if(ObjectUtils.isEmpty(type)) {
            throw new ResourceExceptionHandler(ErrorCode.RESOURCE_EXCEPTION_ERROR);
        }

        String storeFileName = imageName + "." + type;

        // DB 에 존재하는지 파일 찾기
        profileService.checkByStoreFileName(storeFileName);

        // 경로에 있는 파일에 접근해서 파일을 스트림으로 반환
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * request 를 받고 Accept 헤더에 image/jpeg, image/png 인지 확인 후 확장자를 반환하는 메서드
     */
    private String getFileTypeByRequest(HttpServletRequest request) {
        String type = request.getHeader("Accept");
        if(type.equals(ImageCode.JPG.getMimeType())) {
            return ImageCode.JPG.getExt();
        }
        if(type.equals(ImageCode.PNG.getMimeType())) {
            return ImageCode.PNG.getExt();
        }
        return null;
    }
}
