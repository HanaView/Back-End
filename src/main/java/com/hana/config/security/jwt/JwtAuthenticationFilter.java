package com.hana.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    // Bearer
    //JWT 혹은 OAuth에 대한 토큰을 사용한다. (RFC 6750)
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Start JwtAuthenticationFilter -------------------------------------------1");
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        log.info("[JwtAuthenticationFilter] AccessToken 값 추출 완료: {}", token);

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("Start JwtAuthenticationFilter -------------------------------------------2");

//            // Redis 에 해당 accessToken logout 여부 확인
//            String isLogout = (String)redisTemplate.opsForValue().get(token);
//            log.info("Start isLogout -------------------------------------------4 "+ isLogout);
//            log.info("Start isLogout -------------------------------------------5 "+ ObjectUtils.isEmpty(isLogout));
//
//            if (ObjectUtils.isEmpty(isLogout)) {
            if(true){
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // getName은 사용자의 ID를 의미 함
                log.info("Start JwtAuthenticationFilter -------------------------------------------6 "+authentication.getName());
                log.info("Start JwtAuthenticationFilter -------------------------------------------6 "+authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 접근 권한 없으면 AccessDeniedException 발생
        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
