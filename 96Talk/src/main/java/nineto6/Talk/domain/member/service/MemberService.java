package nineto6.Talk.domain.member.service;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import nineto6.Talk.domain.authority.code.Role;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.domain.authority.domain.Authority;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.dto.MemberAuthorityDto;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.domain.member.dto.MemberDto;
import nineto6.Talk.domain.member.controller.request.MemberSaveRequest;
import nineto6.Talk.domain.authority.repository.AuthorityRepository;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.profile.repository.ProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<MemberDto> login(String memberEmail) {
        MemberAuthorityDto memberAuthorityDto = memberRepository
                .findMemberAndAuthByEmail(memberEmail).orElse(null);

        if(ObjectUtils.isEmpty(memberAuthorityDto)) {
            return Optional.empty();
        }

        return Optional.of(memberAuthorityDto.toDto());
    }

    /**
     * 회원가입 용 서비스
     * Exception throw : BusinessExceptionHandler
     */
    @Transactional
    public void signUp(MemberSaveRequest memberSaveRequest) {
        // 중복된 이메일이 있는지 확인
        Member duplicateEmail = memberRepository.findByMemberEmail(memberSaveRequest.getMemberEmail())
                .orElse(null);
        if(!ObjectUtils.isEmpty(duplicateEmail)) {
            throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
        }

        // 중복된 닉네임이 있는지 확인
        Member duplicateNm = memberRepository.findByMemberNickname(memberSaveRequest.getMemberNickname())
                .orElse(null);
        if(!ObjectUtils.isEmpty(duplicateNm)) {
            throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
        }

        Member member = Member.builder()
                .memberEmail(memberSaveRequest.getMemberEmail())
                .memberPwd(passwordEncoder.encode(memberSaveRequest.getMemberPwd())) // Password 인코딩
                .memberNickname(memberSaveRequest.getMemberNickname())
                .build();

        // Member 저장
        memberRepository.save(member);

        // 권한 저장
        Authority authority = Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build();

        authorityRepository.saveAuthority(authority);

        // 기본 프로필 생성
        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();

        profileRepository.saveDefault(profile);
    }

    /**
     * 이메일 중복 체크 로직 (비어있으면 true 이미 있으면 false)
     */
    public boolean duplicateCheckEmail(String memberEmail) {
        Optional<Member> findMember = memberRepository.findByMemberEmail(memberEmail);
        return findMember.isEmpty();
    }

    /**
     * 닉네임 중복 체크 로직 (비어있으면 true 이미 있으면 false)
     */
    public boolean duplicateCheckNickname(String memberNm) {
        Optional<Member> findMember = memberRepository.findByMemberNickname(memberNm);
        return findMember.isEmpty();
    }
}
