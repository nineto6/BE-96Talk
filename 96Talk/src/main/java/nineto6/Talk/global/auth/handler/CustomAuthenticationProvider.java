package nineto6.Talk.global.auth.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.dto.member.MemberDetailsDto;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * 전달받은 사용자의 아이디와 비밀번호를 기반으로 비즈니스 로직을 처리하여 사용자의 ‘인증’에 대해서 검증을 수행하는 클래스
 * CustomAuthenticationFilter 로 부터 생성한 토큰을 통하여 ‘UserDetailsService’를 통해 데이터베이스 내에서 정보를 조회
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserDetailsService userDetailsService;
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("2.CustomAuthenticationProvider");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // 'AuthenticationFilter' 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String memberEmail = token.getName();
        String memberPwd = (String) token.getCredentials();

        // Spring Security - UserDetailsService 를 통해 DB 에서 아이디로 사용자 조회
        MemberDetailsDto memberDetailsDto = (MemberDetailsDto) userDetailsService.loadUserByUsername(memberEmail);

        // passwordEncoder 를 이용하여 userPw 와 DB 에서 조회한 userDetailsDto.getUserPw(인코딩된) 비밀번호를 비교
        if(!(passwordEncoder.matches(memberPwd, memberDetailsDto.getPassword()))) {
            throw new BadCredentialsException(memberDetailsDto.getPassword() + " Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(memberDetailsDto, memberPwd, memberDetailsDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
