package com.hana.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAuthService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean isAuthenticated(String randomKey) {
        String key = randomKey + "3";
        Boolean isAuthenticated = (Boolean) redisTemplate.opsForValue().get(key);
        return isAuthenticated != null && isAuthenticated;
    }

//    public void storeRefreshToken(String userId, String refreshToken) {
//        redisTemplate.opsForValue().set("refreshToken:" + userId, refreshToken);
//    }
//
//    public String getRefreshToken(String userId) {
//        return (String) redisTemplate.opsForValue().get("refreshToken:" + userId);
//    }
}
