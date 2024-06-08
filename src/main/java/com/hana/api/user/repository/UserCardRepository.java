package com.hana.api.user.repository;


import com.hana.api.user.entity.UserCard;
import com.hana.api.user.entity.UserSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    List<UserCard> findByUserId(long userId);
    Optional<UserCard> findByUserIdAndCardId(long userId, long cardId);
}
