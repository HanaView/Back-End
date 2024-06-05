package com.hana.api.saving.entity;

import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.entity.DepositCategory;
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
@Table(name = "saving_category")
@Entity
public class SavingCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_category_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "카테고리명 필수 값입니다.")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "saving_category_id")
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private SavingCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private List<SavingCategory> children = new ArrayList<>();

    @OneToMany(mappedBy = "savingCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Saving> savings = new ArrayList<>();
}
