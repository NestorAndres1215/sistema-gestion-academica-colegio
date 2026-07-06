package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.infrastructure.persistence.entities.AdminEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaAdminRepository extends JpaRepository<AdminEntity,Long> {

    boolean existsByDni(String dni);

    boolean existsByPhone(String phone);

    @Query("""
        SELECT a FROM AdminEntity a
        WHERE (
            :status IS NULL
            OR :status = ''
            OR a.status = :status
        )
        AND (
            :search IS NULL
            OR :search = ''
            OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(a.paternalLastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(a.maternalLastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(a.dni) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(a.phone) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(a.profile) LIKE LOWER(CONCAT('%', :search, '%'))
        )
    """)
    Page<AdminEntity> searchByStatus(
            @Param("status") String status,
            @Param("search") String search,
            Pageable pageable
    );
}
