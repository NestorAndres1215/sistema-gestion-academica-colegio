package com.san_andres.backend.admin.infrastructure.persistence.repository;

import com.san_andres.backend.admin.infrastructure.persistence.entity.AdminEntity;
import com.san_andres.backend.report.infrastructure.persistence.projection.AdministratorReportProjection;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaAdminRepository extends JpaRepository<AdminEntity, Long> {

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
            Pageable pageable);

    @Query("""
                SELECT a
                FROM AdminEntity a
                JOIN a.userEntity u
                WHERE LOWER(u.email) = LOWER(:email)
            """)
    Optional<AdminEntity> findByEmail(@Param("email") String email);

    @Query("""
                SELECT a
                FROM AdminEntity a
                WHERE a.status = 'ACTIVE'
                AND (
                    LOWER(
                        CONCAT(
                            COALESCE(a.firstName, ''), ' ',
                            COALESCE(a.middleName, ''), ' ',
                            COALESCE(a.paternalLastName, ''), ' ',
                            COALESCE(a.maternalLastName, '')
                        )
                    ) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(COALESCE(a.userEntity.username, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(COALESCE(a.userEntity.email, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR COALESCE(a.dni, '') LIKE CONCAT('%', :search, '%')
                )
                ORDER BY a.firstName ASC, a.paternalLastName ASC
            """)
    List<AdminEntity> searchActive(
            @Param("search") String search,
            Pageable pageable);

    @Query(value = """
                SELECT *
                FROM administrator
                WHERE status = 'ACTIVE'
                ORDER BY RAND()
                LIMIT :limit
            """, nativeQuery = true)
    List<AdminEntity> findRandom(@Param("limit") int limit);

}
