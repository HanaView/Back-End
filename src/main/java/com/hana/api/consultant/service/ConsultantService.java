package com.hana.api.consultant.service;

import com.hana.api.auth.dto.request.AuthRequest;
import com.hana.api.auth.dto.response.AuthResponseDto;
import com.hana.api.auth.service.RedisAuthService;
import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.repository.ConsultantRepository;
import com.hana.common.dto.Response;
import com.hana.common.exception.ErrorCode;
import com.hana.common.exception.consultant.ConsultantNotFoundException;
import com.hana.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.hana.common.exception.ErrorCode.CONSULTANT_DUPLICATION;

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

    public ResponseEntity<?> signUp(AuthRequest.ConsultantSignupRequest consultantSignupRequest){

        if (consultantRepository.existsByLoginId(consultantSignupRequest.getLoginId())) {
            return response.fail(CONSULTANT_DUPLICATION, HttpStatus.BAD_REQUEST);
        }

        Consultant consultant =
                Consultant.builder()
                        .loginId(consultantSignupRequest.getLoginId())
                        .password(passwordEncoder.encode(consultantSignupRequest.getPassword()))
                        .role(consultantSignupRequest.getRole())
                        .build();

        consultantRepository.save(consultant);

        return response.success("회원가입 완료");
    }

    public ResponseEntity<?> signIn(AuthRequest.ConsultantAuthRequest consultantAuthRequest) {

        log.info("[SignIn] 회원 정보 요청");

        if (consultantRepository.findByLoginId(consultantAuthRequest.getLoginId()).isEmpty()) {
            return response.fail(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = consultantAuthRequest.toAuthentication();

            log.info("[SignIn] authenticationToken : {}", authenticationToken.toString());

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticationManager에 의해 loadUserByUsername 이 호출되는 부분
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("[SignIn] authentication : {}", authentication.toString());

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            AuthResponseDto authResponseDto = jwtTokenProvider.generateToken(authentication);
            log.info("[SignIn] 토큰 발급 : {}", authResponseDto.toString());

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            // log.info("RT:" + authentication.getName() + " : " + authResponseDto.getRefreshToken() + " : " + TimeUnit.MILLISECONDS);
            // redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
            return response.success(authResponseDto, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return response.fail(ErrorCode.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
    }

    public void deleteConsultant(String loginId) {
        Consultant consultant = consultantRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ConsultantNotFoundException(ErrorCode.CONSULTANT_NOT_FOUND));
        consultantRepository.delete(consultant);
    }
}
