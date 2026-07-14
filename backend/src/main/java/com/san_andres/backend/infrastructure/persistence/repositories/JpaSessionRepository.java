package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import com.san_andres.backend.infrastructure.persistence.projection.ActiveSessionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSessionRepository extends JpaRepository<SessionEntity,Long> {

    @Query("""
        SELECT s
        FROM SessionEntity s
        WHERE s.user.id = :userId
        AND s.isActive = 'ACTIVE'
    """)
    Optional<SessionEntity> findActiveByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT
        s.id AS sessionId,
        u.id AS userId,
        u.username AS username,
        u.email AS email,
        s.loginAt AS loginAt
    FROM SessionEntity s
    JOIN s.user u
    WHERE s.isActive = 'ACTIVE'
      AND (
            :username IS NULL
            OR :username = ''
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))
      )
""")
    Page<ActiveSessionProjection> findActiveSessions(
            @Param("username") String username,
            Pageable pageable
    );


}
