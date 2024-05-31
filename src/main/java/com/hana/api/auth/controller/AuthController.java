package com.hana.api.auth.controller;

import com.hana.api.auth.dto.request.ConsultantAuthRequest;
import com.hana.api.auth.dto.request.ConsultantSignupRequest;
import com.hana.api.auth.dto.request.UserAuthRequest;
import com.hana.api.auth.dto.response.AuthResponseDto;
import com.hana.api.auth.service.RedisAuthService;
import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.service.ConsultantService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "AUTH API", description = "유저(텔러, 사용자)에 대한 인증, 인가를 위한 API")
public class AuthController {

    private final RedisAuthService redisAuthService;
    private final ConsultantService consultantService;

    @Operation(summary = "회원가입", description = "회원가입 하자 좀 제발.")
    @PostMapping("/consultant/register")
    public ResponseEntity<?> registerConsultant(@RequestBody ConsultantSignupRequest registerRequest) {
        return consultantService.signUp(registerRequest);
    }

    @PostMapping("/consultant/login")
    public ResponseEntity<?> createConsultantAuthToken(@RequestBody ConsultantAuthRequest authRequest) {
        log.info(authRequest.toString());
        return consultantService.signIn(authRequest);
    }
}
