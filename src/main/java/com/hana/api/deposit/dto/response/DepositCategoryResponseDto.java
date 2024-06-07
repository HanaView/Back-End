package com.hana.api.deposit.dto.response;

import com.hana.api.card.entity.CardCategory;
import com.hana.api.deposit.entity.DepositCategory;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DepositCategoryResponseDto {

    private Long id;
    private String name;
    private List<DepositCategoryResponseDto> subCategory;

    public DepositCategoryResponseDto(DepositCategory entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.subCategory = entity.getChildren().stream()
                .map(DepositCategoryResponseDto::new)
                .collect(Collectors.toList());
    }
}
