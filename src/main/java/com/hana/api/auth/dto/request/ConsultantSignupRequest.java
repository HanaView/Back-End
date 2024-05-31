package com.hana.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultantSignupRequest {
    private String loginId;
    private String password;
    private String role; // only for Consultant
}
