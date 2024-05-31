package com.hana.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
public class ConsultantAuthRequest {
    private String loginId;  // 사용자 이름 또는 ID
    private String password;  // 비밀번호

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}
