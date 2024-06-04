package com.hana.api.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
}
