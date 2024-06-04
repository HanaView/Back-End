package com.hana.api.saving.entity;

import com.hana.api.deposit.entity.DepositCategory;
import com.hana.api.deposit.entity.DepositRate;
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
@Table(name = "saving")
@Entity
public class Saving extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "적금 상품명은 필수 값입니다.")
    private String name;

    @Column
    private long minJoinAmount;

    @Column
    private long maxJoinAmount;

    @Column(length = 31)
    private String target;

    @Column(length = 255)
    private String adImg;

    @Column(length = 255)
    private String infoImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_category_id", referencedColumnName = "saving_category_id")
    private SavingCategory savingCategory;

    @OneToMany(mappedBy = "saving", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SavingRate> savingRates = new ArrayList<>();
}
