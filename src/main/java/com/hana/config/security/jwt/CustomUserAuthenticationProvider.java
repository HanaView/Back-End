package com.hana.config.security.jwt;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.exception.ErrorCode;
import com.hana.common.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("[CustomUserAuthenticationProvider] user 인증 시작");

        String username = authentication.getName();
        Long userId = null;

        if (username.startsWith("USER_")) {
            userId = Long.valueOf(username.substring("USER_".length()));
        } else {
            //DaoAuthenticationProvider(userDetailsService 기반) 로 처리하기 위해 예외로 던짐.
            throw new UsernameNotFoundException("Invalid username prefix");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (user != null) {
            UserDetails userDetails =
                    new org.springframework.security.core.userdetails.User(user.getName(), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_user")));
            // 사용자 정보가 올바르게 로드된 경우 인증 성공 처리
            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Authentication failed for " + username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

