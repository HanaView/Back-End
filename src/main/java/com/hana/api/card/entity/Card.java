package com.hana.api.card.entity;

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
@Table(name = "card")
@Entity
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "카드명은 필수 값입니다.")
    private String name;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "연회비는 필수 값입니다.")
    private String annualFee;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "브랜드는 필수 값입니다.")
    private String brand;

    @Column(nullable = false, length = 30)
    private String spending;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "카드 정보 pdf는 필수 값입니다.")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_category_id", referencedColumnName = "card_category_id")
    private CardCategory category;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CardBenefit> benefits = new ArrayList<>();
}
