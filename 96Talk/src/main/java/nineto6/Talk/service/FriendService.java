package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.domain.Friend;
import nineto6.Talk.domain.Member;
import nineto6.Talk.model.frined.FriendRequest;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.repository.FriendRepository;
import nineto6.Talk.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public void createFriend(MemberDto memberDto, FriendRequest friendRequest) {
        // 이름으로 된 친구가 등록되어 있는지 확인
        Member friendMember = memberRepository.findByMemberNm(friendRequest.getFriendNickname())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR));

        // 이미 등록되어 있는 친구인지 확인
        Optional<Friend> friendOptional = friendRepository.findByMemberIdAndFriendMemberId(memberDto.getMemberId(), friendMember.getMemberId());

        if(friendOptional.isPresent()) {
            // 이미 등록되어 있는 경우 Exception
            throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        Friend friend = Friend.builder()
                .memberId(memberDto.getMemberId())
                .friendMemberId(friendMember.getMemberId())
                .build();

        friendRepository.save(friend);
    }

    @Transactional
    public void removeFriend(MemberDto memberDto, FriendRequest friendRequest) {
        // 이름으로 된 친구가 등록되어 있는지 확인
        Member friendMember = memberRepository.findByMemberNm(friendRequest.getFriendNickname())
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
