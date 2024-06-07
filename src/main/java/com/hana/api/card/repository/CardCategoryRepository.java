package com.hana.api.card.repository;

import com.hana.api.card.entity.CardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardCategoryRepository extends JpaRepository<CardCategory, Long> {
    List<CardCategory> findAllByParentIdIsNull();
}
