package com.hana.config.security.jwt;

//import com.hana.api.auth.dto.response.AuthResponseDto;
//import com.hana.api.user.dto.response.UserResponseDto;
import com.hana.api.auth.dto.response.AuthResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    // JWT 토큰 구성에 필요한 상수들
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;              // 60분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일

    private final Key key;

    // 생성자를 통해 시크릿 키를 디코딩하고 설정
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        log.info("[JwtTokenProvider] 문자열 Byte 형식으로 변환 -> keyBytes : {}", keyBytes);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("Secret Key는 32자 이상으로 설정해주세요.");
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("[key] 암호 : {}", key);
    }

    // 유저 정보를 이용해 AccessToken과 RefreshToken을 생성하는 메서드
    public AuthResponseDto generateToken(Authentication authentication) {

        log.info("[generateToken] 토큰 발급을 위한 유저 정보 : {}", authentication.toString());

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("[generateToken] authorities 권한 가져오기 : {}", authorities);

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("[generateToken] 액세스 토큰 생성 완료: {}", accessToken);


        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("[generateToken] 리프레쉬 토큰 생성 완료: {}", refreshToken);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }


    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {

        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        log.info("[getAuthentication] claims 정보 조회 : {} ", claims);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        log.info("[getAuthentication] Subject 정보 조회 : {}", claims.getSubject());
        log.info("[getAuthentication] authorities 정보 조회 : {}", authorities.toString());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        log.info("[getAuthentication] user 정보 조회 : {}", principal);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {

        try {
            log.info("[validateToken] 토큰 유효성 검사 : {}", Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).toString());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // 토큰을 파싱하여 클레임을 가져오는 메서드
    private Claims parseClaims(String accessToken) {

        try {
            log.info("[parseClaims] Claims : {}", Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().toString());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰의 남은 유효 시간을 반환하는 메서드
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        log.info("[getExpiration] 유효 시간 검증 : {}", expiration);
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
