package com.hana.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    private String name; // only for User
    private String tele; // only for User
    private String socialNumber; // only for User
}
