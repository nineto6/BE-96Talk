package nineto6.Talk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.SuccessCode;
import nineto6.Talk.controller.swagger.FriendControllerDocs;
import nineto6.Talk.model.friend.FriendRequest;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.profile.ProfileResponse;
import nineto6.Talk.model.response.ApiResponse;
import nineto6.Talk.service.FriendService;
import nineto6.Talk.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController implements FriendControllerDocs {
    private final FriendService friendService;
    private final ProfileService profileService;

    /**
     * 친구 프로필 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getFriendProfiles(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        List<ProfileResponse> friendProfiles = profileService.findFriendProfiles(memberDetailsDto.getMemberDto());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(friendProfiles)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * 친구 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createFriend(@RequestBody FriendRequest friendRequest,
                                                    @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        friendService.createFriend(memberDetailsDto.getMemberDto(), friendRequest);

        ApiResponse success = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.INSERT_SUCCESS.getStatus())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(success, SuccessCode.INSERT_SUCCESS.getHttpStatus());
    }

    /**
     * 친구 삭제
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> removeFriend(@RequestBody FriendRequest friendRequest,
                                                    @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        friendService.removeFriend(memberDetailsDto.getMemberDto(), friendRequest);

        ApiResponse success = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.DELETE_SUCCESS.getStatus())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return new ResponseEntity<>(success, SuccessCode.DELETE_SUCCESS.getHttpStatus());
    }
}

