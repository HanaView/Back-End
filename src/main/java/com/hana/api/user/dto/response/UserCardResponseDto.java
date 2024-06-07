package com.hana.api.user.dto.response;

import com.hana.api.card.dto.response.CardResponseDto;
import com.hana.api.user.entity.UserCard;
import com.hana.api.user.entity.UserDeposit;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
public class UserCardResponseDto {
    private Long id;
    private UserResponse userInfo;
    private CardResponseDto cardInfo;
    private UserDepositResponseDto userDepositInfo;
    private LocalDateTime createdAt;

    public UserCardResponseDto(UserCard entity) {
        this.id = entity.getId();
        this.userInfo = new UserResponse(entity.getUser());
        this.cardInfo = new CardResponseDto(entity.getCard());
        this.userDepositInfo = new UserDepositResponseDto(entity.getUserDeposit());
        this.createdAt = entity.getCreatedDate();
    }
}
