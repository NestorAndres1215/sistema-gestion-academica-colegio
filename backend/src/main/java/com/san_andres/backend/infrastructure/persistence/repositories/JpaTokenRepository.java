package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.infrastructure.persistence.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaTokenRepository extends JpaRepository<TokenEntity,Long> {
    @Query(
            value = "DELETE FROM token WHERE session_id = :sessionId",
            nativeQuery = true
    )
    void deleteBySessionId(@Param("sessionId") Long sessionId);
}
