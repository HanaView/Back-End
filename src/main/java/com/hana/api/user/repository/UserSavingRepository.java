package com.hana.api.user.repository;


import com.hana.api.user.entity.UserSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSavingRepository extends JpaRepository<UserSaving, Long> {

}
