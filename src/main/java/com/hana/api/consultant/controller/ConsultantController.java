package com.hana.api.consultant.controller;


import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.service.UserService;
import com.hana.common.dto.Response;
import com.hana.common.util.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tellers")
@RestController
@Tag(name = "Teller API", description = "상담원 로그인 아웃 처리")
public class ConsultantController {

    private final UserService userService;
    private final Response response;
}
