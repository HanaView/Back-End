package com.hana.api.user.repository;

import com.hana.api.deposit.entity.Deposit;
import com.hana.api.user.entity.UserDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDepositRepository extends JpaRepository<UserDeposit, Long> {
    List<UserDeposit> findByUserId(long userId);
    List<UserDeposit> findByUserIdAndDeposit(long userId, Deposit deposit);

}
