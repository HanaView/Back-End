package com.hana.api.card.dto.response;

import com.hana.api.card.entity.CardBenefit;
import com.hana.api.deposit.entity.DepositRate;
import lombok.Data;
import lombok.ToString;

@Data
public class CardBenefitResponseDto {

    private String content;

    public CardBenefitResponseDto(CardBenefit entity) {
        this.content = entity.getContent();
    }
}
