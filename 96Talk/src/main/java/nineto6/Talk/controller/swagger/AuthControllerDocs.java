package nineto6.Talk.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberLoginRequest;
import nineto6.Talk.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "인증")
public interface AuthControllerDocs {
    @Operation(summary = "로그인", description = "로그인용 메서드입니다.")
    void login(MemberLoginRequest memberLoginRequest);
    @Operation(summary = "토큰 재발급", description = "헤더에 Refresh-Token 쿠키가 필요합니다.")
    ResponseEntity<ApiResponse> reissue(@Schema(description = "RT") String refreshToken,
                                               HttpServletRequest request,
                                               HttpServletResponse response);
    @Operation(summary = "로그아웃", description = "헤더에 Access-Token 토큰이 필요합니다.")
    ResponseEntity<ApiResponse> logout(@Schema(hidden = true) String authorization,
                                       @Schema(hidden = true) MemberDetailsDto memberDetailsDto,
                                       HttpServletResponse response);
}
