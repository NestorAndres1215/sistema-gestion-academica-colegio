package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSessionRepository extends JpaRepository<SessionEntity,Long> {

    @Query(
            value = """
                        SELECT *
                        FROM session s
                        WHERE s.user_id = :userId
                        AND s.is_active = 'ACTIVE'
                    """,
            nativeQuery = true
    )
    Optional<SessionEntity> findActiveByUserId(@Param("userId") Long userId);


}
