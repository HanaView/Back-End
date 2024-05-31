package com.hana.api.consultant.entity;

import com.hana.api.card.entity.Card;
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
@Table(name = "task_category")
@Entity
public class TaskCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_category_id")
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @OneToMany(mappedBy = "taskCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ConsultingInfo> consultingInfos = new ArrayList<>();
}
