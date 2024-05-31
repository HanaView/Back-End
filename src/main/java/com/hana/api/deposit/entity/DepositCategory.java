package com.hana.api.deposit.entity;

import com.hana.api.card.entity.CardCategory;
import com.hana.api.consultant.entity.ConsultingInfo;
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
@Table(name = "deposit_category")
@Entity
public class DepositCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_category_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "카테고리명 필수 값입니다.")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "deposit_category_id")
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private DepositCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private List<DepositCategory> children = new ArrayList<>();

    @OneToMany(mappedBy = "depositCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Deposit> deposits = new ArrayList<>();
}
