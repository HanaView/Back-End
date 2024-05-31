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
@Table(name = "card_category")
@Entity
public class CardCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_category_id")
    private Long id;

    @Column(nullable = false, length = 31)
    @NotBlank(message = "카테고리명은 필수 값입니다.")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "card_category_id")
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private CardCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private List<CardCategory> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Card> cards = new ArrayList<>();
}
