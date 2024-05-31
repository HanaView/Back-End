package com.hana.api.consultant.entity;

import com.hana.api.card.entity.CardBenefit;
import com.hana.api.card.entity.CardCategory;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Getter
@Table(name = "consultant")
@Entity
public class Consultant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultant_id")
    private Long id;

    @Column(nullable = false, length = 127)
    @NotBlank(message = "로그인 아이디는 필수 값입니다.")
    private String loginId;

    @Column(nullable = false, length = 127)
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "role는 필수 값입니다.")
    private String role;

    @OneToMany(mappedBy = "consultant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ConsultingInfo> consultingInfos = new ArrayList<>();

    @OneToMany(mappedBy = "consultant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ConsultantReview> consultantReviews = new ArrayList<>();
}
