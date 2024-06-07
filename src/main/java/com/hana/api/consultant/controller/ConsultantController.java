package com.hana.api.consultant.controller;


import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserResponse;
import com.hana.api.user.service.UserService;
import com.hana.common.dto.Response;
import com.hana.common.util.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static com.hana.common.exception.ErrorCode.USER_NOT_AUTHENTICATION;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tellers")
@RestController
@Tag(name = "Teller API", description = "상담원 로그인 아웃 처리")
public class ConsultantController {

    private final UserService userService;
    private final Response response;

    @Operation(summary = "본인인증된 user 정보 불러오기", description = "Key를 통해 Redis 2에서 User 정보를 조회 후, 데이터 불러오기")
    @GetMapping("/userInfo")
    public ResponseEntity<?> getUser(@RequestParam("key") String key) {
        System.out.println(key);
        UserResponse user = userService.getUser(key + "2");

        if(user == null)
            return response.fail(USER_NOT_AUTHENTICATION, HttpStatus.BAD_REQUEST);

        return response.success(user, HttpStatus.OK);
    }
}
