package com.hana.api.deposit.entity;

import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Getter
@Table(name = "deposit_rate")
@Entity
public class DepositRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_rate_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "예금 기간은 필수 값입니다.")
    private Long period;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "기간별 이자율은 필수 값입니다.")
    private double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id", referencedColumnName = "deposit_id")
    private Deposit deposit;
}
