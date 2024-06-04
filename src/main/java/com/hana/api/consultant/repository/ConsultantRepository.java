package com.hana.api.consultant.repository;

import com.hana.api.consultant.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
    // loginId로 Consultant 찾기
    Optional<Consultant> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}