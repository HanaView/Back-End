package com.hana.api.saving.repository;

import com.hana.api.deposit.entity.Deposit;
import com.hana.api.saving.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long>{

    Optional<Saving> getDepositById(Long id);
}
