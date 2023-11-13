package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.codes.Role;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.domain.Authority;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.MemberAuthority;
import nineto6.Talk.model.member.MemberDto;
import nineto6.Talk.model.member.MemberSaveRequest;
import nineto6.Talk.repository.AuthorityRepository;
import nineto6.Talk.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private  final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<MemberDto> login(String memberEmail) {
        MemberAuthority memberAuthority = memberRepository
                .findMemberAndAuthByEmail(memberEmail)
                .orElseThrow(() -> new AuthenticationServiceException(memberEmail));

        return Optional.of(memberAuthority.toDto());
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
        Member duplicateNm = memberRepository.findByMemberNm(memberSaveRequest.getMemberNm())
                .orElse(null);
        if(!ObjectUtils.isEmpty(duplicateNm)) {
            throw new BusinessExceptionHandler(ErrorCode.DUPLICATE_ERROR);
        }

        Member member = Member.builder()
                .memberEmail(memberSaveRequest.getMemberEmail())
                .memberPwd(passwordEncoder.encode(memberSaveRequest.getMemberPwd())) // Password 인코딩
                .memberNm(memberSaveRequest.getMemberNm())
                .build();

        // Member 저장
        memberRepository.save(member);

        // 권한 저장
        Authority authority = Authority.builder()
                .memberId(member.getMemberId())
                .authorityRole(Role.USER.getAuth())
                .build();

        authorityRepository.saveAuthority(authority);
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
        Optional<Member> findMember = memberRepository.findByMemberNm(memberNm);
        return findMember.isEmpty();
    }
}
