package com.hana.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequest {
    private String randomKey;
}
