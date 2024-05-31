package com.hana.api.saving.dto.response;

import com.hana.api.saving.entity.Saving;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class SavingResponseDto {
    private Long id;
    private String name;
    private Long minJoinAmount;
    private Long maxJoinAmount;
    private String target;
    private String adImg;
    private String infoImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long savingCategoryId;
    private List<SavingRateResponseDto> savingRates;

    public SavingResponseDto(Saving entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.minJoinAmount = entity.getMinJoinAmount();
        this.maxJoinAmount = entity.getMaxJoinAmount();
        this.target = entity.getTarget();
        this.adImg = entity.getAdImg();
        this.infoImg = entity.getInfoImg();
        this.createdAt = entity.getCreatedDate();
        this.updatedAt = entity.getModifiedDate();
        this.savingCategoryId = entity.getSavingCategory().getId();
        this.savingRates = entity.getSavingRates().stream()
                .map(SavingRateResponseDto::new)
                .collect(Collectors.toList());
    }
}
