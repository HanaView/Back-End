package com.hana.api.card.dto.response;

import com.hana.api.card.entity.Card;
import com.hana.api.deposit.entity.Deposit;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CardResponseDto {
    private Long id;
    private String name;
    private String annualFee;
    private String brand;
    private String link;
    private List<CardBenefitResponseDto> cardBenefits;

    public CardResponseDto(Card entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.annualFee = entity.getAnnualFee();
        this.brand = entity.getBrand();
        this.link = entity.getLink();
        this.cardBenefits = entity.getBenefits().stream()
                .map(CardBenefitResponseDto::new)
                .collect(Collectors.toList());
    }
}
