package nineto6.Talk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.ImageCode;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.controller.swagger.ProfileControllerDocs;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.profile.ProfileResponse;
import nineto6.Talk.model.response.ApiResponse;
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
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerDocs {
    private final ProfileService profileService;

    /**
     * 프로필 조회
     */
    @GetMapping("/{memberNm}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable("memberNm") String memberNm) {
        ProfileResponse profileResponse = profileService.findByNickname(memberNm);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(profileResponse)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * 프로필 상태 메세지 수정
     */
    @PutMapping()
    public ResponseEntity<ApiResponse> updateProfileStateMessage(@RequestParam("stateMessage") String stateMessage,
                                                                 @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        profileService.updateStateMessage(memberDetailsDto.getMemberDto(), stateMessage);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.UPDATE_SUCCESS.getStatus())
                .message(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * 프로필 이미지 수정
     */
    @PutMapping(value = "/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> updateProfileImageFile(@RequestPart MultipartFile imageFile,
                                                              @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        profileService.updateImageFile(memberDetailsDto.getMemberDto(), imageFile);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.UPDATE_SUCCESS.getStatus())
                .message(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     *  프로필 이미지 다운로드
     */
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable("fileName") String fileName,
                                  HttpServletRequest request) throws IOException {
        String type = getFileTypeByRequest(request);

        if(ObjectUtils.isEmpty(type)) {
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        String storeFileName = fileName + "." + type;

        // DB 에 존재하는지 파일 찾기
        profileService.checkByStoreFileName(storeFileName);

        // 경로에 있는 파일에 접근해서 파일을 스트림으로 반환
        return new UrlResource("file:" + storeFileName);
    }

    /**
     * request 를 받고 Accept 헤더에 image/jpeg, image/png 인지 확인 후 jpg 및 png 를 반환하는 메서드
     */
    private String getFileTypeByRequest(HttpServletRequest request) {
        String type = request.getHeader("Accept");
        if(type.equals(ImageCode.JPG.getCode())) {
            return ImageCode.JPG.getName();
        }
        if(type.equals(ImageCode.PNG.getCode())) {
            return ImageCode.PNG.getName();
        }
        return null;
    }
}