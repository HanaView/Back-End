package com.hana.api.saving.repository;

import com.hana.api.saving.entity.Saving;
import com.hana.api.saving.entity.SavingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingCategoryRepository extends JpaRepository<SavingCategory, Long>{

}
