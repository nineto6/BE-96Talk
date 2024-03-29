package nineto6.Talk.controller.profile.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.dto.member.MemberDetailsDto;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Tag(name = "프로필")
public interface ProfileControllerDocs {
    @Operation(summary = "자기 프로필 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getProfile(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "프로필 단일 조회", description = "토큰이 필요합니다.")

    ResponseEntity<ApiResponse> getProfileByMemberNickname(String memberNickname);
    @Operation(summary = "프로필 닉네임으로 검색", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getSearchProfile(String keyword,
                                                 Integer page,
                                                 Integer recordSize,
                                                 Integer pageSize);
    @Operation(summary = "프로필 상태 메세지 삭제", description = "수정 메서드, 토큰이 필요합니다.")
    ResponseEntity<ApiResponse> deleteProfileStateMessage(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "프로필 이미지 및 상태메세지 수정", description = "수정 메서드, 토큰이 필요합니다.")
    ResponseEntity<ApiResponse> updateProfileImageFileAndStateMessage(MultipartFile imageFile,
                                                                      String profileStateMessage,
                                                       @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "프로필 이미지 삭제", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> deleteProfileImage(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "프로필 이미지 리소스 단일 조회", description = "토큰이 필요합니다.")
    ResponseEntity<Resource> downloadImage(String fileName,
                           HttpServletRequest request) throws IOException;
}
