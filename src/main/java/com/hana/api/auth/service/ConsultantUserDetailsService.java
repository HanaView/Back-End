package com.hana.api.auth.service;

import com.hana.api.consultant.entity.Consultant;
import com.hana.api.consultant.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ConsultantUserDetailsService implements UserDetailsService {

    private final ConsultantRepository consultantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 ID로 Consultant 찾기
        Consultant consultant = consultantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Consultant not found"));

        // UserDetails 객체로 반환
        return new User(consultant.getLoginId(), consultant.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + consultant.getRole())));
    }
}
