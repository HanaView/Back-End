package com.hana.api.auth.controller;

import com.hana.api.auth.dto.request.AuthRequest;
import com.hana.api.auth.service.RedisAuthService;
import com.hana.api.auth.service.TokenService;
import com.hana.api.consultant.service.ConsultantService;
import com.hana.api.user.service.UserService;
import com.hana.common.dto.Response;
import com.hana.common.util.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "유저(텔러, 사용자)에 대한 인증, 인가를 위한 API")
public class AuthController {

    private final RedisAuthService redisAuthService;
    private final UserService userService;
    private final TokenService tokenService;
    private final ConsultantService consultantService;
    private final Response response;

    @Operation(summary = "회원가입", description = "아이디와 비밀번호를 입력해주세요.")
    @PostMapping("/consultant/register")
    public ResponseEntity<?> registerConsultant(@Validated @RequestBody AuthRequest.ConsultantSignupRequest registerRequest, Errors errors) {

        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return consultantService.signUp(registerRequest);
    }

    @Operation(summary = "회원가입", description = "이름과 전화번호 그리고 주민번호를 입력해주세요.")
    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody AuthRequest.UserSignupRequest registerRequest, Errors errors) {

        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.signUp(registerRequest);
    }

    @PostMapping("/consultant/login")
    public ResponseEntity<?> loginConsultant(@Validated @RequestBody AuthRequest.ConsultantAuthRequest authRequest, Errors errors) {

        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        log.info("[loginConsultant] : {}", authRequest.getLoginId());
        return consultantService.signIn(authRequest);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue( AuthRequest.Reissue reissue, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return tokenService.reissue(reissue);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout( AuthRequest.Logout logout, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return tokenService.logout(logout);
    }

    // test용 API
    @GetMapping("/list")
    public ResponseEntity<?> list(@AuthenticationPrincipal User user){
        log.info(user.toString());
        log.info(user.getAuthorities().toString());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_teller")) {
                return response.success(HttpStatus.OK);
            }
        }
        return response.success(user.toString(), HttpStatus.OK);
    }
}
