package com.hana.api.deposit.dto.response;

import com.hana.api.deposit.entity.Deposit;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class DepositResponseDto {
    private Long id;
    private String name;
    private Long minJoinAmount;
    private Long maxJoinAmount;
    private String target;
    private String adImg;
    private String infoImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long depositCategoryId;
    private List<DepositRateResponseDto> depositRates;

    public DepositResponseDto(Deposit entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.minJoinAmount = entity.getMinJoinAmount();
        this.maxJoinAmount = entity.getMaxJoinAmount();
        this.target = entity.getTarget();
        this.adImg = entity.getAdImg();
        this.infoImg = entity.getInfoImg();
        this.createdAt = entity.getCreatedDate();
        this.updatedAt = entity.getModifiedDate();
        this.depositCategoryId = entity.getDepositCategory().getId();
        this.depositRates = entity.getDepositRates().stream()
                .map(DepositRateResponseDto::new)
                .collect(Collectors.toList());
    }
}
