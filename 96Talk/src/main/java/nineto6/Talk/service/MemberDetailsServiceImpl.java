package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.dto.member.MemberDetailsDto;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberService memberService;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        // 사용자 정보가 존재하지 않는 경우 예외 처리
        if(memberEmail == null || memberEmail.equals("")) {
            return memberService.login(memberEmail)
                    .map(MemberDetailsDto::new)
                    .orElseThrow(() -> new AuthenticationServiceException(memberEmail));
        }

        // 비밀번호가 맞지 않는 경우 예외 처리
        else {
            return memberService.login(memberEmail)
                    .map(MemberDetailsDto::new)
                    .orElseThrow(() -> new BadCredentialsException(memberEmail));
        }
    }
}
