package com.hana.api.deposit.entity;

import com.hana.api.consultant.entity.TaskCategory;
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
@Table(name = "deposit")
@Entity
public class Deposit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "예금 상품명은 필수 값입니다.")
    private String name;

    @Column
    private Long minJoinAmount;

    @Column
    private Long maxJoinAmount;

    @Column(length = 31)
    private String target;

    @Column(length = 255)
    private String adImg;

    @Column(length = 255)
    private String infoImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_category_id", referencedColumnName = "deposit_category_id")
    private DepositCategory depositCategory;

    @OneToMany(mappedBy = "deposit", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DepositRate> depositRates = new ArrayList<>();
}
