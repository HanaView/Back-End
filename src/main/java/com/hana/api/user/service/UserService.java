package com.hana.api.user.service;



import com.hana.api.auth.dto.request.UserAuthRequest;
import com.hana.api.auth.dto.request.UserSignupRequest;
import com.hana.api.auth.service.RedisAuthService;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserResponseDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.dto.Response;
import com.hana.common.util.MessageUtil;
import com.hana.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.hana.common.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final RedisAuthService redisAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisTemplate<String,Object> createRedisTemplate;


    public ResponseEntity<?> auth(UserRequestDto.Auth auth) {
        User user = userRepository.findByNameAndTele(auth.getName(), auth.getTele()).orElse(null);
        //user = User.builder().name("박병철").id(1L).socialNumber("230829-012486").tele("010-1234-5678").build();
        if(user == null)
            return response.fail(USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        // 2. random key 발급
        String randomKey = UUID.randomUUID().toString().substring(0, 10);
        log.info("key" + randomKey);
        // 3. redis random key and dto
        ValueOperations<String, Object> valueOperations = createRedisTemplate.opsForValue();
        valueOperations.set(randomKey + "1", user);
        //valueOperations.set(randomKey + "1", user, 10, TimeUnit.MINUTES);
        //4. 문자전송
        //MessageUtil.Sendmsg(randomKey, auth.getTele());

        return response.success(randomKey, HttpStatus.OK);
    }

    public ResponseEntity<?> authComplete(User user, String key) {
        ValueOperations<String, Object> valueOperations = createRedisTemplate.opsForValue();
        valueOperations.set(key + "2", user);
        return response.success(key, HttpStatus.OK);
    }

    public User getUser(String key) {
        ValueOperations<String, Object> valueOperations = createRedisTemplate.opsForValue();
        return (User) valueOperations.get(key);
    }

    public User registerUser(UserSignupRequest signupRequest) {
        User user =
                User.builder()
                        .name(signupRequest.getName())
                        .tele(signupRequest.getTele())
                        .socialNumber(signupRequest.getSocialNumber())
                        .build();
        return userRepository.save(user);
    }

    public Optional<User> findByNameAndTel(String name, String tel) {
        return userRepository.findByNameAndTele(name, tel);
    }

    public boolean validateUser(UserAuthRequest userAuthRequest) {
        return redisAuthService.isAuthenticated(userAuthRequest.getRandomKey());
    }

    public void deleteUser(String name, String tel) {
        User user = userRepository.findByNameAndTele(name, tel)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
//        if (userRepository.findByName(login.getEmail()).orElse(null) == null) {
//            return response.fail(USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
//        }
//
//        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
//        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
//        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
//
//        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
//        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//        // 3. 인증 정보를 기반으로 JWT 토큰 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
//        log.info("RT:" + authentication.getName()+" : "+tokenInfo.getRefreshToken()+" : "+tokenInfo.getRefreshTokenExpirationTime()+" : "+TimeUnit.MILLISECONDS);
//
//
//        createRedisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
//
//        return response.success(tokenInfo, HttpStatus.OK);
//    }
//
//
//    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
//        // 1. Refresh Token 검증
//        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
//            return response.fail(INVALID_REFRESHTOKEN, HttpStatus.BAD_REQUEST);
//        }
//
//        // 2. Access Token 에서 User email 을 가져옵니다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());
//
//        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
//        String refreshToken = (String)createRedisTemplate.opsForValue().get("RT:" + authentication.getName());
//        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
//        if(ObjectUtils.isEmpty(refreshToken)) {
//            return response.fail(NOT_FOUND_REFRESHTOKEN, HttpStatus.BAD_REQUEST);
//        }
//        if(!refreshToken.equals(reissue.getRefreshToken())) {
//            return response.fail(INVALID_REFRESHTOKEN, HttpStatus.BAD_REQUEST);
//        }
//
//        // 4. 새로운 토큰 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        // 5. RefreshToken Redis 업데이트
//        createRedisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
//
//        return response.success(tokenInfo, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> logout(UserRequestDto.Logout logout) {
//        // 1. Access Token 검증
//        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
//            return response.fail(INVALID_ACCESSTOKEN, HttpStatus.BAD_REQUEST);
//        }
//
//        // 2. Access Token 에서 User email 을 가져옵니다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
//
//        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
//        if (createRedisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
//            // Refresh Token 삭제
//            createRedisTemplate.delete("RT:" + authentication.getName());
//        }
//
//        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
//        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
//        createRedisTemplate.opsForValue()
//                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
//        // 5. 이후 JwtAuthenticationFilter 에서 redis에 있는 logout 정보를 가지고 와서 접근을 거부함
//
//        return response.success("로그아웃 되었습니다.");
//    }
}
