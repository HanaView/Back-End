package com.hana.api.saving.dto.response;

import com.hana.api.deposit.entity.DepositRate;
import com.hana.api.saving.entity.SavingRate;
import lombok.Data;
import lombok.ToString;

@Data

@ToString
public class SavingRateResponseDto {
    private Long id;
    private Long period;
    private Double rate;

    public SavingRateResponseDto(SavingRate entity) {
        this.id = entity.getId();
        this.period = entity.getPeriod();
        this.rate = entity.getRate();
    }
}
