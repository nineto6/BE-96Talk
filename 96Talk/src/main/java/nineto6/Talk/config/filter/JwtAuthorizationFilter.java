package nineto6.Talk.config.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.AuthConstants;
import nineto6.Talk.common.utils.TokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 유효성 검증을 수행하며 직접적인 사용자 '인증'을 확인 후 '인가'를 합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // [STEP1] Client 에서 API 를 요청할 때 Header 를 확인합니다.
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        log.debug("[JwtAuthorizationFilter] header Check: {}", header);

        // [STEP2-1] Header 내에 토큰이 존재하는 경우
        if (header != null && !header.equalsIgnoreCase("")) {

            // [STEP2-2] Header 내에 토큰을 추출합니다.
            String token = TokenUtils.getAccessTokenFormHeader(header);

            // [STEP3-1] 추출한 엑세스 토큰이 유효한지 여부를 체크합니다.
            if (token != null && TokenUtils.isValidAccessToken(token)) {

                // [STEP4] 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
                String memberEmail = TokenUtils.getMemberEmailFormAccessToken(token);

                // [STEP5] 사용자 아이디가 존재하는지 여부 체크 (null 값, 공백 값이 아닐 경우)
                if (!ObjectUtils.isEmpty(memberEmail) && !memberEmail.equalsIgnoreCase("")) {
                    // 인증에 성공하면 SecurityContextHolder 에 인증된 Authentication 객체를 집어 넣음으로써 인가한다.
                    Authentication auth = TokenUtils.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    log.info("[JwtAuthorizationFilter] Jwt 토큰 허가, SecurityContextHolder 에 인증 등록!!");
                    log.info("[JwtAuthorizationFilter] 현재 사용자의 이메일 : {}", auth.getName());
                    log.info("[JwtAuthorizationFilter] 현재 사용자의 권한 : {}", auth.getAuthorities());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}