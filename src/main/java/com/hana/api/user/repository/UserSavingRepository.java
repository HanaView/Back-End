package com.hana.api.user.repository;


import com.hana.api.deposit.entity.Deposit;
import com.hana.api.saving.entity.Saving;
import com.hana.api.user.entity.UserDeposit;
import com.hana.api.user.entity.UserSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSavingRepository extends JpaRepository<UserSaving, Long> {
    List<UserSaving> findByUserId(long userId);
    List<UserSaving> findByUserIdAndSaving(long userId, Saving saving);

}
