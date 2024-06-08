package com.hana.api.card.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CardRequestDto {

    @Data
    public static class CardRegisterRequest {
        private String name;
        private String annualFee;
        private String brand;
        private String link;
        private String spending;
        private Long cardCategoryId;
    }

    @Data
    public static class CardCategoryRegisterRequest {
        private String name;
        private Long parentId;
    }

    @Data
    public static class CardBenefitRegisterRequest {
        private String content;
        private Long cardId;
    }
}
