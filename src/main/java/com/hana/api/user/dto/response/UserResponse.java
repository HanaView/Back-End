package com.hana.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hana.api.user.entity.User;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private Long id;
    private String name;
    private String tele;
    private String socialNumber;

    public UserResponse(User entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.tele = entity.getTele();
        this.socialNumber = entity.getSocialNumber();
    }
}
