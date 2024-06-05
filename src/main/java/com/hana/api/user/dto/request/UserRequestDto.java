package com.hana.api.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;


public class UserRequestDto {

    @Getter
    @Setter
    public static class Auth {
        @NotEmpty(message = "이름은 필수 입력값입니다.")
        //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String name;

        @NotEmpty(message = "전화번호는 필수 입력값입니다.")
        //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String tele;
    }
    @Getter
    @Setter
    public static class UserDepositRequestDto {
        @NotNull(message = "예금 상품명은 필수 값입니다.")
        private Long userId;
        @NotNull(message = "잔액은 필수 값입니다.")
        private Long balance;
        @NotNull(message = "가입 기간은 필수 값입니다.")
        private long period;
        @NotEmpty(message = "비밀번호는 필수 값입니다.")
        private String password;
        private Long userDepositId2;
    }

    @Getter
    @Setter
    public static class UserSavingRequestDto {
        @NotNull(message = "유저 ID는 필수 값입니다.")
        private Long userId;
        @Positive(message = "가입 기간은 양수여야 합니다.")
        private Long period;
        @NotEmpty(message = "비밀번호는 필수 값입니다.")
        private String password;
        @NotNull(message = "연결 계좌 ID는 필수 값입니다.")
        private Long userDepositId;
        @NotNull(message = "납입금액은 필수 값입니다.")
        private Long perMonth;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Ocr {
        public MultipartFile image;
    }
}
