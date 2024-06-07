package com.hana.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hana.api.consultant.entity.ConsultantReview;
import com.hana.api.consultant.entity.ConsultingInfo;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "유저 이름은 필수 값입니다.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "유저 전화번호는 필수 값입니다.")
    private String tele;

    @Column(nullable = false)
    @NotBlank(message = "유저 주민번호는 필수 값입니다.")
    private String socialNumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<ConsultingInfo> consultingInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<UserCard> userCards = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<UserDeposit> userDeposits = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<UserSaving> userSavings = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<ConsultantReview> consultantReviews = new ArrayList<>();
}
