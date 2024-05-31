package com.hana;

import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;

@SpringBootTest
@ContextConfiguration(classes = HanaViewApplication.class)
@Slf4j
class HanaViewApplicationTests {

    @Autowired
    private UserService usersService;
    @Test
    public void authRedis() {
        UserRequestDto.Auth auth = new UserRequestDto.Auth();
        auth.setName("바보");
        auth.setTele("01080195950");
        Object obj = usersService.auth(auth);
    }

}
