package com.hana.api.user.entity;


import com.hana.api.deposit.entity.Deposit;
import com.hana.api.saving.entity.Saving;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Getter
@Table(name = "user_saving")
@Entity
public class UserSaving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_saving_id")
    private Long id;

    @Column(nullable = false, length = 31)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance;

    @Column(nullable = false)
    private Long bounds;

    @Column(nullable = false, length = 15)
    private String password;

    @Column(nullable = false)
    private Long period;

    @Column(nullable = false)
    private Long perMonth;

    @Column(nullable = false)
    private Boolean isHuman;

    @Column(nullable = false)
    private Boolean isLoss;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id", referencedColumnName = "saving_id")
    private Saving saving;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_deposit_id", referencedColumnName = "user_deposit_id")
    private UserDeposit userDeposit;
}
