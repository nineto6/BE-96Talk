package nineto6.Talk.domain.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.domain.friend.domain.Friend;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.friend.controller.request.FriendRequest;
import nineto6.Talk.domain.member.dto.MemberDto;
import nineto6.Talk.domain.friend.repository.FriendRepository;
import nineto6.Talk.domain.member.repository.MemberRepository;
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
        Member friendMember = memberRepository.findByMemberNickname(friendRequest.getFriendNickname())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 이미 등록되어 있는 친구인지 확인
        Optional<Friend> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMember.getMemberId());

        if(friendOptional.isPresent()) {
            // 이미 등록되어 있는 경우 Exception
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        Friend friend = Friend.builder()
                .memberId(memberDto.getMemberId())
                .friendMemberId(friendMember.getMemberId())
                .build();

        friendRepository.save(friend);
    }

    public void removeFriend(MemberDto memberDto, FriendRequest friendRequest) {
        // 이름으로 된 친구가 등록되어 있는지 확인
        Member friendMember = memberRepository.findByMemberNickname(friendRequest.getFriendNickname())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        // 이미 등록되어 있는 친구인지 확인
        Optional<Friend> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMember.getMemberId());

        if(friendOptional.isEmpty()) {
            // 등록되어 있지 않을 경우 Exception
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        friendRepository.deleteByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMember.getMemberId());
    }
}
