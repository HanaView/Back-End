package com.hana.api.card.dto.response;

import com.hana.api.card.entity.CardCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CardCategoryResponseDto {

    private Long id;
    private String name;
    private List<CardCategoryResponseDto> subCategory;

    public CardCategoryResponseDto(CardCategory entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.subCategory = entity.getChildren().stream()
                .map(CardCategoryResponseDto::new)
                .collect(Collectors.toList());
    }
}
