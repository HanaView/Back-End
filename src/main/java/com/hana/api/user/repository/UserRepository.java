package com.hana.api.user.repository;


import com.hana.api.user.entity.User;
import com.hana.api.user.entity.UserDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // socialNumber로 User 찾기
    Optional<User> findBySocialNumber(String socialNumber);
    Optional<User> findByName(String name);
    Optional<User> findByNameAndTele(String name, String tele);
}
