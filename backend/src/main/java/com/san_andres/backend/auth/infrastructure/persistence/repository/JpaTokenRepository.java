package com.san_andres.backend.auth.infrastructure.persistence.repository;

import com.san_andres.backend.auth.infrastructure.persistence.entity.TokenEntity;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaTokenRepository extends JpaRepository<TokenEntity, Long> {

    @Modifying
    @Transactional
    void deleteBySession_Id(Long sessionId);

    @Query("""
                SELECT s.isActive AS status
                FROM TokenEntity t
                JOIN t.session s
                WHERE s.user.id = :userId
                  AND s.isActive = 'ACTIVE'
            """)
    List<TokenStatusProjection> findActiveTokenStatusByUserId(
            @Param("userId") Long userId);

    Optional<TokenEntity> findByToken(String token);
}
