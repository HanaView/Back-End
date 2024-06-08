package com.hana.api.deposit.repository;

import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.entity.DepositRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositRateRepository extends JpaRepository<DepositRate, Long>{
}
