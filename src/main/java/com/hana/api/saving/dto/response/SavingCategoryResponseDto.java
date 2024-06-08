package com.hana.api.saving.dto.response;

import com.hana.api.deposit.entity.DepositCategory;
import com.hana.api.saving.entity.SavingCategory;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SavingCategoryResponseDto {

    private Long id;
    private String name;
    private List<SavingCategoryResponseDto> subCategory;

    public SavingCategoryResponseDto(SavingCategory entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.subCategory = entity.getChildren().stream()
                .map(SavingCategoryResponseDto::new)
                .collect(Collectors.toList());
    }
}
