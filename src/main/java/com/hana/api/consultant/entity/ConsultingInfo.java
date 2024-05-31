package com.hana.api.consultant.entity;

import com.hana.api.card.entity.CardCategory;
import com.hana.api.user.entity.User;
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
@Table(name = "consulting_info")
@Entity
public class ConsultingInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulting_info_id")
    private Long id;

    @Column(nullable = false, length = 127)
    @NotBlank(message = "상담 제목은 필수 값입니다.")
    private String title;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "상담 내용은 필수 값입니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id", referencedColumnName = "consultant_id")
    private Consultant consultant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_category_id", referencedColumnName = "task_category_id")
    private TaskCategory taskCategory;
}
