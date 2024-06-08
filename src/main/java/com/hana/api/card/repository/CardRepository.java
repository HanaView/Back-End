package com.hana.api.card.repository;

import com.hana.api.card.entity.Card;
import com.hana.api.consultant.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> getCardsByCategoryId(long id);
}
