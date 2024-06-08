package com.hana.api.saving.repository;

import com.hana.api.saving.entity.SavingCategory;
import com.hana.api.saving.entity.SavingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRateRepository extends JpaRepository<SavingRate, Long>{

}
