package com.hana.api.user.dto.response;

import com.hana.api.deposit.dto.response.DepositResponseDto;
import com.hana.api.saving.dto.response.SavingRateResponseDto;
import com.hana.api.saving.dto.response.SavingResponseDto;
import com.hana.api.saving.entity.Saving;
import com.hana.api.user.entity.UserSaving;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class UserSavingResponseDto {
    private Long id;
    private String accountNumber;
    private Long balance;
    private Long bounds;
    private String password;
    private Long period;
    private Long perMonth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDepositResponseDto parent;
    private SavingResponseDto savingInfo;
    private UserResponse userInfo;

    public UserSavingResponseDto(UserSaving entity){
        this.id = entity.getId();
        this.accountNumber = entity.getAccountNumber();
        this.balance = entity.getBalance();
        this.bounds = entity.getBounds();
        this.password = entity.getPassword();
        this.perMonth = entity.getPerMonth();
        this.period = entity.getPeriod();
        if(entity.getUserDeposit() != null) {
            this.parent = new UserDepositResponseDto(entity.getUserDeposit());
        }
        this.savingInfo = new SavingResponseDto(entity.getSaving());
        this.userInfo = new UserResponse(entity.getUser());
        this.createdAt = entity.getSaving().getCreatedDate();
        this.updatedAt = entity.getSaving().getModifiedDate();
    }
}
