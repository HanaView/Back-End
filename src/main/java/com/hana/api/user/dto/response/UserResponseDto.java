package com.hana.api.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserResponse{
        private Long id;
        private String name;
        private String tele;
        private String socialNumber;
    }

}
