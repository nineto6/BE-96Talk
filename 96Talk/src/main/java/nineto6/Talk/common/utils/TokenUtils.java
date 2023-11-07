package nineto6.Talk.common.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.common.codes.ErrorCode;
import nineto6.Talk.common.exception.BusinessExceptionHandler;
import nineto6.Talk.config.JwtToken;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.member.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT 관련된 토큰 Util
 */
@Slf4j
@Component
public class TokenUtils {

    // application.properties 에서 accessSecretKey 값 가져오기
    private static String accessSecretKey;
    private static String refreshSecretKey;

    @Value(value = "${custom.jwt-access-secret-key}")
    public void setAccessSecretKey(String key) {
        accessSecretKey = key;
    }

    @Value(value = "${custom.jwt-refresh-secret-key}")
    public void setRefreshSecretKey(String key) {
        refreshSecretKey = key;
    }

    /**
     * 사용자 정보를 기반으로 토큰을 생성하여 반환 해주는 메서드
     */
    public static JwtToken generateJwtToken(MemberDto memberDto) {
        // 사용자 시퀀스를 기준으로 JWT 토큰을 발급하여 반환해줍니다.
        JwtBuilder accessBuilder = Jwts.builder()
                .setHeader(createHeader())                                             // Header 구성
                .setClaims(createAccessClaims(memberDto))                                // Payload - Claims 구성
                .setSubject("AccessToken")                                             // Payload - Subject 구성
                .signWith(SignatureAlgorithm.HS256, createSignature(accessSecretKey))  // Signature 구성
                .setExpiration(createAccessTokenExpiredDate());                        // Expired Date 구성

        JwtBuilder refreshBuilder = Jwts.builder()
                .setHeader(createHeader())                                             // Header 구성
                .setClaims(createRefreshClaims(memberDto))                               // Payload - Claims 구성
                .setSubject("RefreshToken")                                            // Payload - Subject 구성
                .signWith(SignatureAlgorithm.HS256, createSignature(refreshSecretKey)) // Signature 구성
                .setExpiration(createRefreshTokenExpiredDate());                       // Expired Date 구성

        return JwtToken.builder()
                .AccessToken(accessBuilder.compact())
                .RefreshToken(refreshBuilder.compact())
                .build();
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 AuthenticationToken(인증 토큰)으로 반환하는 메서드
     */
    public static Authentication getAuthentication(String accessToken) {
        Claims claims = getAccessTokenToClaimsFormToken(accessToken);

        if(claims.get("auth") == null || claims.get("auth").toString().equals("")) {
            throw new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_ERROR);
        }

        // 클레임에서 권한 정보 가져오기
        List<String> auths = getCreateRoleStrings(claims);

        // UserDetails 객체를 만들어서 Authentication 리턴
        MemberDetailsDto principal = new MemberDetailsDto(MemberDto
                .builder()
                .memberEmail(claims.get("email").toString())
                .roleList(auths)
                .build());

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    /**
     * JWT 토큰 안에 Claims 의 Auth 는 "ROLE_"이 없기 때문에 Spring Security 전용으로 "ROLE_" 문자열을 붙여서 반환하는 메서드
     */
    private static List<String> getCreateRoleStrings(Claims claims) {
        return Arrays.stream(claims.get("auth") // claims 에서 "auth" 로 된 권한 정보를 가져와서
                        .toString() // 문자열 변환
                        .split(","))// "USER, ADMIN" -> ["USER", "ADMIN"]
                .collect(Collectors.toList())// List 로 받아서 반환
                .stream() // 스트림
                .map("ROLE_"::concat) // 각 "USER" -> "ROLE_USER", "ADMIN" -> "ROLE_ADMIN" 으로 변환
                .collect(Collectors.toList()); // List 로 받아서 반환
    }


    /**
     * 엑세스 토큰을 기반으로 사용자 정보를 반환 해주는 메서드
     * @param token String : 토큰
     * @return String : 사용자 정보
     */
    public static String parseAccessTokenToUserInfo(String token) {
        return Jwts.parser()
                .setSigningKey(accessSecretKey)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

    /**
     * 유효한 엑세스 토큰인지 확인 해주는 메서드
     * @param token String  : 토큰
     * @return      boolean : 유효한지 여부 반환
     */
    public static boolean isValidAccessToken(String token) {
        try {
            Claims claims = getAccessTokenToClaimsFormToken(token);

            log.info("expireTime : {}", claims.getExpiration());
            log.info("userId : {}", claims.get("email"));
            log.info("userNm : {}", claims.get("nickname"));

            return true;
        } catch (JwtException | NullPointerException exception) {
            return false;
        }
    }

    /**
     * 유효한 리프레쉬 토큰인지 확인 해주는 메서드
     * @param token String  : 토큰
     * @return      boolean : 유효한지 여부 반환
     */
    public static boolean isValidRefreshToken(String token) {
        try {
            Claims claims = getRefreshTokenToClaimsFormToken(token);

            log.info("expireTime : {}", claims.getExpiration());
            log.info("userId : {}", claims.get("email"));
            log.info("userNm : {}", claims.get("nickname"));


            return true;
        } catch (JwtException | NullPointerException exception) {
            return false;
        }
    }

    /**
     * Header 내에 토큰을 추출합니다.
     *
     * @param header 헤더
     * @return String
     */
    public static String getTokenFormHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * 엑세스 토큰의 만료기간을 지정하는 함수
     * @return Calendar
     */
    private static Date createAccessTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 30);   // 30분으로 설정
        return c.getTime();
    }

    /**
     * 리프레쉬 토큰의 만료기간을 지정하는 함수
     * @return Calendar
     */
    private static Date createRefreshTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 3);   // 3일로 설정
        return c.getTime();
    }

    /**
     * JWT 의 "헤더" 값을 생성해주는 메서드
     * @return HashMap<String, Object>
     */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    /**
     * Access-Token 전용 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     */
    private static Map<String, Object> createAccessClaims(MemberDto memberDto) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", memberDto.getMemberEmail());
        claims.put("nickname", memberDto.getMemberNm());

        StringBuilder authority = getRemoveRoleStrings(memberDto);
        claims.put("auth", authority);
        return claims;
    }

    /**
     * JWT 토큰 안에 Claims 의 Auth 에 "ROLE_"을 없애고 권한들을 각각 ','와 함께 StringBuilder 로 더해서 반환하는 메서드
     */
    private static StringBuilder getRemoveRoleStrings(MemberDto memberDto) {
        List<String> collect = memberDto.getRoleList()
                .stream()// ROLE_USER
                .map(string -> string.split("_")[1]) // "ROLE", "USER" -> [1] -> "USER"
                .collect(Collectors.toList()); // List 받아서 반환

        StringBuilder authority = new StringBuilder();
        collect.forEach(string -> // StringBuilder 로 List 안에 존재하는 권한들을 ","와 함께 문자열 더하기
                authority
                        .append(string)
                        .append(","));
        return authority;
    }

    /**
     * Refresh-Token 전용 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     */
    private static Map<String, Object> createRefreshClaims(MemberDto memberDto) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", memberDto.getMemberEmail());
        claims.put("nickname", memberDto.getMemberNm());

        return claims;
    }

    /**
     * JWT "서명(Signature)" 발급을 해주는 메서드
     * @return Key
     */
    private static Key createSignature(String key) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 엑세스 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     * @param token : 토큰
     * @return Claims : Claims
     */
    private static Claims getAccessTokenToClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(accessSecretKey))
                .parseClaimsJws(token).getBody();
    }

    /**
     * 리프레쉬 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     * @param token : 토큰
     * @return Claims : Claims
     */
    private static Claims getRefreshTokenToClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(refreshSecretKey))
                .parseClaimsJws(token).getBody();
    }


    /**
     * 엑세스 토큰을 기반으로 사용자 이메일을 반환받는 메서드
     * @param token : 토큰
     * @return String : 사용자 이메일
     */
    public static String getMemberEmailFormAccessToken(String token) {
        Claims claims = getAccessTokenToClaimsFormToken(token);
        return claims.get("email").toString();
    }

    /**
     * 엑세스 토큰을 기반으로 사용자 닉네임을 반환받는 메서드
     * @param token : 토큰
     * @return String : 사용자 닉네임
     */
    public static String getMemberNmFormAccessToken(String token) {
        Claims claims = getAccessTokenToClaimsFormToken(token);
        return claims.get("nickname").toString();
    }

    /**
     * 엑세스 토큰을 기반으로 만료 기간을 반환받는 메서드 (만료 시간 - 현재 시간 = 남은 시간(ms))
     * @param token
     * @return Long : Expiration
     */
    public static Long getExpirationFormAccessToken(String token) {
        Claims claims = getAccessTokenToClaimsFormToken(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }
}