package com.hana.api.auth.service;

import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.repository.ConsultantRepository;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.exception.ErrorCode;
import com.hana.common.exception.consultant.ConsultantNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ConsultantRepository consultantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] : {}", username);
        if (username.startsWith("CONSULTANT_")) {
            String consultantLoginId = username.substring("CONSULTANT_".length());
            return loadConsultantByLoginId(consultantLoginId);
        } else {
            throw new UsernameNotFoundException("Invalid username prefix");
        }
    }

    private UserDetails loadConsultantByLoginId(String loginId) {
        Consultant consultant = consultantRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ConsultantNotFoundException(ErrorCode.CONSULTANT_NOT_FOUND));
        return new User(consultant.getLoginId(), consultant.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + consultant.getRole())));
    }
}
