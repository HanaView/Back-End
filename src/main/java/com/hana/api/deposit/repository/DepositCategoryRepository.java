package com.hana.api.deposit.repository;

import com.hana.api.card.entity.CardCategory;
import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.entity.DepositCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositCategoryRepository extends JpaRepository<DepositCategory, Long>{
    List<DepositCategory> findAllByParentIdIsNull();
}
