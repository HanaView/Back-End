package com.hana.api.consultant.service;

import com.hana.api.auth.dto.request.ConsultantAuthRequest;
import com.hana.api.auth.dto.request.ConsultantSignupRequest;
import com.hana.api.auth.dto.response.AuthResponseDto;
import com.hana.api.auth.service.RedisAuthService;
import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.repository.ConsultantRepository;
import com.hana.common.dto.Response;
import com.hana.common.exception.ErrorCode;
import com.hana.common.exception.user.UserNotFoundException;
import com.hana.config.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultantService {

    private final ConsultantRepository consultantRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisAuthService redisAuthService;
    private final AuthenticationManager authenticationManager;
    private final Response response;

    public ResponseEntity<?> signUp(ConsultantSignupRequest consultantSignupRequest){

        Consultant consultant =
                Consultant.builder()
                        .loginId(consultantSignupRequest.getLoginId())
                        .password(passwordEncoder.encode(consultantSignupRequest.getPassword()))
                        .role(consultantSignupRequest.getRole())
                        .build();

        consultantRepository.save(consultant);

        return response.success("회원가입 완료");
    }

    public ResponseEntity<?> signIn(ConsultantAuthRequest consultantAuthRequest) {

        log.info("[SignIn] signDataHandler 로 회원 정보 요청");

        if (consultantRepository.findByLoginId(consultantAuthRequest.getLoginId()).isEmpty()) {
            return response.fail(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = consultantAuthRequest.toAuthentication();

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info(authentication.toString());

            log.info("[signIn] AuthResponseDto 객체 생성");
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            AuthResponseDto authResponseDto = jwtTokenProvider.generateToken(authentication);
            log.info("[signIn] 토큰 발급 : {}", authResponseDto.toString());
            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            log.info("RT:" + authentication.getName() + " : " + authResponseDto.getRefreshToken() + " : " + TimeUnit.MILLISECONDS);

            // redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(authResponseDto, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return response.fail(ErrorCode.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
    }

//    public ResponseEntity<?> refreshConsultantToken(String refreshToken) {
//        String username = jwtUtil.extractUsername(refreshToken);
//        String storedRefreshToken = redisAuthService.getRefreshToken(username);
//
//        if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
//            String newAccessToken = jwtUtil.generateToken(username);
//            return new AuthResponse(newAccessToken, refreshToken);
//        } else {
//            throw new RuntimeException("Invalid refresh token");
//        }
//    }

//    public ResponseEntity<?> signIn(ConsultantAuthRequest consultantAuthRequest){
//
//        log.info("[SignIn] signDataHandler 로 회원 정보 요청");
//
//        Consultant consultant = consultantRepository.findByLoginId(consultantAuthRequest.getLoginId())
//                .orElseThrow(() -> new UserNotFoundException("등록되지 않은 회원입니다.", ErrorCode.USER_NOT_FOUND));
//
//        log.info("[getSignInResult] 패스워드 비교 수행");
//        if (!passwordEncoder.matches(consultantAuthRequest.getPassword(), consultant.getPassword())) {
//            throw new RuntimeException("패스워드 불일치");
//        }

//        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
//        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
//        UsernamePasswordAuthenticationToken authenticationToken = consultantAuthRequest.toAuthentication();
//
//        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
//        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
//        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);
//
//        // 3. 인증 정보를 기반으로 JWT 토큰 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        log.info("[signIn] AuthResponseDto 객체 생성");
//        AuthResponseDto signInResultDto = AuthResponseDto.builder()
//                .accessToken(jwtTokenProvider.createToken(String.valueOf(user.getEmail()), user.getRoles()))
//                .build();
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(consultantAuthRequest.getLoginId(), consultantAuthRequest.getPassword())
//        );
//
//        log.info(authentication.toString());
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String username = authRequest.getUsername();
//        log.info(username);
//
//        String accessToken = Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + 600_000)) // 10 minutes
//                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs")
//                .compact();
//
//        String refreshToken = Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + 1_209_600_000)) // 14 days
//                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs")
//                .compact();
//
//        redisAuthService.storeRefreshToken(username, refreshToken);
//    }

    public Optional<Consultant> findByLoginId(String loginId) {
        return consultantRepository.findByLoginId(loginId);
    }

    public void deleteConsultant(String loginId) {
        Consultant consultant = consultantRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));
        consultantRepository.delete(consultant);
    }
}
