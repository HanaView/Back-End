package com.hana.api.auth.service;

import com.hana.api.auth.dto.request.AuthRequest;
import com.hana.api.auth.dto.response.AuthResponseDto;
import com.hana.common.dto.Response;
import com.hana.common.exception.ErrorCode;
import com.hana.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private  final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final Response response;

    public ResponseEntity<?> reissue(AuthRequest.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail(ErrorCode.INVALID_ACCESSTOKEN, HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 인증 정보를  가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 Consultant의 Login_id 를 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail(ErrorCode.NOT_FOUND_REFRESHTOKEN, HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail(ErrorCode.INVALID_REFRESHTOKEN, HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        AuthResponseDto authResponseDto = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), authResponseDto.getRefreshToken(), authResponseDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(authResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> logout(AuthRequest.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail(ErrorCode.INVALID_ACCESSTOKEN, HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 인증 정보를 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
        // 5. 이후 JwtAuthenticationFilter 에서 redis에 있는 logout 정보를 가지고 와서 접근을 거부함

        return response.success("로그아웃 되었습니다.");
    }
}
