package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.infrastructure.persistence.entities.UserStoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserStoryRepository extends JpaRepository<UserStoryEntity,Long> {

    @Query("""
    SELECT u
    FROM UserStoryEntity u
    WHERE u.user.email = :email
      AND (:status IS NULL OR u.status = :status)
      AND (:action IS NULL OR LOWER(u.action) LIKE LOWER(CONCAT('%', :action, '%')))
""")
    Page<UserStoryEntity> findWithFilters(
            @Param("email") String email,
            @Param("status") UserStatus status,
            @Param("action") String action,
            Pageable pageable
    );

}