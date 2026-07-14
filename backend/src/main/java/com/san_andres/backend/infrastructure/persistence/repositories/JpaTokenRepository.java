package com.san_andres.backend.infrastructure.persistence.repositories;


import com.san_andres.backend.infrastructure.persistence.entities.TokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface JpaTokenRepository extends JpaRepository<TokenEntity,Long> {
    @Modifying
    @Transactional
    void deleteBySession_Id(Long sessionId);
}
