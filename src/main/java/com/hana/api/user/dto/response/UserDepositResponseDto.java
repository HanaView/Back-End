package com.hana.api.user.dto.response;

import com.hana.api.deposit.dto.response.DepositRateResponseDto;
import com.hana.api.deposit.dto.response.DepositResponseDto;
import com.hana.api.deposit.entity.Deposit;
import com.hana.api.user.entity.User;
import com.hana.api.user.entity.UserDeposit;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class UserDepositResponseDto {
    private Long id;
    private String accountNumber;
    private Long balance;
    private Long bounds;
    private String password;
    private Long period;
    private Boolean isHuman;
    private Boolean isLoss;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDepositResponseDto parent;
    private DepositResponseDto depositInfo;
    private UserResponse userInfo;

    public UserDepositResponseDto(UserDeposit entity) {
        this.id = entity.getId();
        this.accountNumber = entity.getAccountNumber();
        this.balance = entity.getBalance();
        this.bounds = entity.getBounds();
        this.password = entity.getPassword();
        this.period = entity.getPeriod();
        this.isHuman = entity.getIsHuman();
        this.isLoss = entity.getIsLoss();
        this.createdAt = entity.getCreatedDate();
        this.updatedAt = entity.getModifiedDate();
        this.userInfo = new UserResponse(entity.getUser());
        this.depositInfo = new DepositResponseDto(entity.getDeposit());
        if(entity.getParent() != null) {
            this.parent = new UserDepositResponseDto(entity.getParent());
        }
    }
}
