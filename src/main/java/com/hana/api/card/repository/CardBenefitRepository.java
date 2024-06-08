package com.hana.api.card.repository;

import com.hana.api.card.entity.Card;
import com.hana.api.card.entity.CardBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {
}
