package com.hana.api.user.controller;


import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserResponse;
import com.hana.api.user.dto.response.UserResponseDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.service.UserService;
import com.hana.common.dto.Response;
import com.hana.common.util.FileUpload;
import com.hana.common.util.Helper;
import com.hana.common.util.OCR;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/login")
@RestController
@Tag(name = "Login Open API", description = "사용자 등록 및 로그인 아웃 처리")
public class UserController {

    private final UserService userService;
    private final Response response;
    @Operation(summary = "로그인", description = "이메일 비번 입력 <br> token 리턴 ")
    // GET 일때
    //    @Parameter(name = "email", description = "이메일 입력", )
    //    @Parameter(name = "password", description = "비빌번호 입력")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserRequestDto.Login login, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return response.success();
    }

//    @PostMapping("/reissue")
//    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue, Errors errors) {
//        // validation check
//        if (errors.hasErrors()) {
//            return response.invalidFields(Helper.refineErrors(errors));
//        }
//        return userService.reissue(reissue);
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(UserRequestDto.Logout logout, Errors errors) {
//        // validation check
//        if (errors.hasErrors()) {
//            return response.invalidFields(Helper.refineErrors(errors));
//        }
//        return userService.logout(logout);
//    }

    @Operation(summary = "인증", description = "이름 폰번호을 통해 사용자 인증 받기")
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@Validated @RequestBody UserRequestDto.Auth auth, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.auth(auth);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/ocr")
    public ResponseEntity<?> ocrimpl(UserRequestDto.Ocr ocrDto, @RequestParam("file") MultipartFile uploadImg, @RequestParam("key") String key,HttpSession session, Errors errors) throws IOException {
        try {
            // Redis에 저장할 키
            String redisKey = key + "3";
            session.setAttribute("key", key);
            UserResponse user = userService.getUser(key + "1");
            // 이미지 파일을 바이트 배열로 변환하여 Redis에 저장
            redisTemplate.opsForValue().set(redisKey, uploadImg.getBytes());

            ocrDto.setImage(uploadImg);

            log.info("Uploaded image name: " + ocrDto.getImage().getOriginalFilename());
            // 이미지네임
            String imgname = "uploads/" + ocrDto.getImage().getOriginalFilename();
//            String imgname = "uploads/example2.jpg";

            // Redis에서 파일을 가져와 OCR 처리
            String imageString = (String) redisTemplate.opsForValue().get(redisKey);
            byte[] imageBytes = imageString.getBytes(StandardCharsets.UTF_8);
            if (imageBytes != null) {
//                FileUpload.saveFile(ocrDto.getImage());
                FileUpload.saveFile(uploadImg);

                JSONObject jsonObject = OCR.getResult(imgname);
                log.info("OCR result: " + jsonObject.toString()); // JSON 객체 로깅
                if (jsonObject == null || !jsonObject.containsKey("images")) {
                    log.info("---------------에러에러-----------------");
                    log.error("OCR result does not contain 'images' key or is null");
                    return response.invalidFields(Helper.refineErrors(errors));
                }

                Map<String, Object> map = OCR.getData(jsonObject);
                map.put("user", user);
                map.put("key", key);
                log.info("map 정보" + map);
                return response.success(map);
            } else {
                log.info("---------------에러에러-----------------");
                log.info("Failed to retrieve image from Redis");
                return response.invalidFields(Helper.refineErrors(errors));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("---------------에러에러-----------------");
            log.info("File upload failed");
            return response.invalidFields(Helper.refineErrors(errors));
        }
    }

    @Operation(summary = "인증", description = "인증 완료 후 User 정보 Redis 2에 저장")
    @GetMapping("/authComplete")
    public ResponseEntity<?> auth(@RequestParam("key") String key) {
        System.out.println(key);
        UserResponse user = userService.getUser(key + "1");
        userService.authComplete(user, key);
        return response.success(key);
    }

}
