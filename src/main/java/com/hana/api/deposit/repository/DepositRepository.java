package com.hana.api.deposit.repository;

import com.hana.api.deposit.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>{
    Optional<Deposit> getDepositById(Long id);
}
