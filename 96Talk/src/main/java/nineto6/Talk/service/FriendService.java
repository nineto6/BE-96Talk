package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.vo.FriendVo;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.controller.friend.request.FriendRequest;
import nineto6.Talk.dto.member.MemberDto;
import nineto6.Talk.repository.friend.FriendRepository;
import nineto6.Talk.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    public void createFriend(MemberDto memberDto, FriendRequest friendRequest) {
        // 자기 자신을 친구 추가 할 경우 Exception
        if(memberDto.getMemberNickname().equals(friendRequest.getFriendNickname())) {
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        // 이름으로 된 친구가 등록되어 있는지 확인
        MemberVo friendMemberVo = memberRepository.findByMemberNickname(friendRequest.getFriendNickname())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 이미 등록되어 있는 친구인지 확인
        Optional<FriendVo> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMemberVo.getMemberId());

        if(friendOptional.isPresent()) {
            // 이미 등록되어 있는 경우 Exception
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        FriendVo friendVo = FriendVo.builder()
                .memberId(memberDto.getMemberId())
                .friendMemberId(friendMemberVo.getMemberId())
                .build();

        friendRepository.save(friendVo);
    }

    public void removeFriend(MemberDto memberDto, FriendRequest friendRequest) {
        // 이름으로 된 친구가 등록되어 있는지 확인
        MemberVo friendMemberVo = memberRepository.findByMemberNickname(friendRequest.getFriendNickname())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        // 이미 등록되어 있는 친구인지 확인
        Optional<FriendVo> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMemberVo.getMemberId());

        if(friendOptional.isEmpty()) {
            // 등록되어 있지 않을 경우 Exception
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        friendRepository.deleteByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMemberVo.getMemberId());
    }
}
