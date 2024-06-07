package com.hana.api.deposit.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DepositRequestDto {

    @Data
    public static class DepositRegisterRequest {
        @NotBlank(message = "예금 상품명은 필수 값입니다.")
        private String name;

        private Long minJoinAmount;

        private Long maxJoinAmount;

        private String target; //어떤

        private String adImg;

        private String infoImg;

        @NotNull(message = "예금 카테고리 ID는 필수 값입니다.")
        private Long depositCategoryId;
    }

    @Data
    public static class DepositCategoryRegisterRequest {
        private String name;
        private Long parentId;
    }

    @Data
    public static class DepositRateRegisterRequest {
        private Long period;
        private Double rate;
        private Long depositId;
    }
}
