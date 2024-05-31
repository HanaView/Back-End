package com.hana.api.deposit.dto.response;

import com.hana.api.deposit.entity.DepositRate;
import lombok.Data;
import lombok.ToString;

@Data

@ToString
public class DepositRateResponseDto {
    private Long id;
    private Long period;
    private Double rate;

    public DepositRateResponseDto(DepositRate entity) {
        this.id = entity.getId();
        this.period = entity.getPeriod();
        this.rate = entity.getRate();
    }
}
