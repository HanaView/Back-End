package com.hana.api.user.entity;

import com.hana.api.deposit.entity.Deposit;
import com.hana.common.entity.BaseEntity;
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
@Table(name = "user_deposit")
@Entity
public class UserDeposit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_deposit_id")
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
    private Boolean isHuman;

    @Column(nullable = false)
    private Boolean isLoss;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id", referencedColumnName = "deposit_id")
    private Deposit deposit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_deposit_id2", referencedColumnName = "user_deposit_id")
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private UserDeposit parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude  // Lombok의 순환 참조 문제를 피하기 위해
    private List<UserDeposit> children = new ArrayList<>();

    @OneToMany(mappedBy = "userDeposit", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude // Lombok의 순환 참조 문제를 피하기 위해
    private List<UserSaving> userSavings = new ArrayList<>();

    public void updateIsLoss(boolean status){
        this.isLoss = status;
    }

    public void updateBalance(Long balance ){
        this.balance = balance;
    }

    public void updateDormancy(boolean status){
        this.isHuman = status;
    }
    @Override
    public String toString() {
        return "UserDeposit{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", bounds=" + bounds +
                ", password='" + password + '\'' +
                ", period=" + period +
                ", isHuman=" + isHuman +
                ", isLoss=" + isLoss +
                ", user=" + (user != null ? "User[id=" + user.getId() + "]" : "null") +
                ", deposit=" + (deposit != null ? "Deposit[id=" + deposit.getId() + "]" : "null") +
                ", parent=" + (parent != null ? "UserDeposit[id=" + parent.getId() + "]" : "null") +
                '}';
    }
}
