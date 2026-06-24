package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByName(String name);

    @Query("""
        SELECT r
        FROM RoleEntity r
        WHERE (:search IS NULL
               OR :search = ''
               OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<RoleEntity> search(
            @Param("search") String search,
            Pageable pageable
    );

}