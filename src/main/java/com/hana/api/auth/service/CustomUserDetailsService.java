package com.hana.api.auth.service;

import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.repository.ConsultantRepository;
import com.hana.api.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        log.info("[loadUserByUsername] : {}", username);
//
//        // 로그인 ID로 Consultant 찾기
//        Consultant consultant = consultantRepository.findByLoginId(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Consultant not found"));
//
//        // UserDetails 객체로 반환
//        return new User(consultant.getLoginId(), consultant.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + consultant.getRole())));
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(Long userId) throws UsernameNotFoundException {
//
//        log.info("[loadUserByUsername] : {}", userId);
//
//        // 로그인 ID로 Consultant 찾기
//        com.hana.api.user.entity.User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("Consultant not found"));
//
//        // UserDetails 객체로 반환
//        return new User(user.getName(), user.getTele(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + "user")));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[loadUserByUsername]");
        if (username.startsWith("CONSULTANT_")) {
            String consultantLoginId = username.substring("CONSULTANT_".length());
            return loadConsultantByLoginId(consultantLoginId);
        } else if (username.startsWith("USER_")) {
            Long userId = Long.valueOf(username.substring("USER_".length()));
            return loadUserById(userId);
        } else {
            throw new UsernameNotFoundException("Invalid username prefix");
        }
    }

    private UserDetails loadConsultantByLoginId(String loginId) {
        Consultant consultant = consultantRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("Consultant not found"));
        return new User(consultant.getLoginId(), consultant.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + consultant.getRole())));
    }

    public UserDetails loadUserById(Long userId) {
        com.hana.api.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info(user.toString());
        return new User(user.getName(), "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_user")));
    }
}
