package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.AdminEntity;
import com.san_andres.backend.infrastructure.persistence.projection.PercentageStatisticProjection;
import com.san_andres.backend.infrastructure.persistence.projection.StatisticProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
            Pageable pageable
    );

    @Query(value = """
        SELECT *
        FROM administrator
        WHERE status = 'ACTIVE'
        ORDER BY RAND()
        LIMIT :limit
    """, nativeQuery = true)
    List<AdminEntity> findRandom(@Param("limit") int limit);

    // Total administradores
    @Query("""
        SELECT 
            'Total administradores' AS label,
            COUNT(a) AS quantity
        FROM AdminEntity a
    """)
    StatisticProjection countAdministrators();


    // Administradores activos
    @Query("""
        SELECT 
            'Administradores activos' AS label,
            COUNT(a) AS quantity
        FROM AdminEntity a
        WHERE a.status = 'ACTIVE'
    """)
    StatisticProjection countActiveAdministrators();


    // Administradores inactivos
    @Query("""
        SELECT 
            'Administradores inactivos' AS label,
            COUNT(a) AS quantity
        FROM AdminEntity a
        WHERE a.status = 'INACTIVE'
    """)
    StatisticProjection countInactiveAdministrators();


    // Registros del último mes
    @Query(value = """
        SELECT
            'Registros último mes' AS label,
            COUNT(*) AS quantity
        FROM administrator a
        INNER JOIN users u ON a.user_id = u.id
        WHERE u.created_at >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01')
          AND u.created_at < DATE_FORMAT(CURDATE(), '%Y-%m-01')
    """, nativeQuery = true)
    StatisticProjection countRegisteredLastMonth();

    // Administradores por género
    @Query("""
        SELECT
            CASE 
                WHEN a.gender = 'MALE' THEN 'Masculino'
                WHEN a.gender = 'FEMALE' THEN 'Femenino'
                ELSE 'Otro'
            END AS label,
            COUNT(a) AS quantity
        FROM AdminEntity a
        GROUP BY a.gender
    """)
    List<StatisticProjection> countByGender();

    // Registros últimos 6 meses
    @Query(value = """
        SELECT
            MONTHNAME(u.created_at) AS label,
            COUNT(a.id) AS quantity
        FROM administrator a
        INNER JOIN users u ON a.user_id = u.id
        WHERE u.created_at >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
        GROUP BY MONTH(u.created_at), MONTHNAME(u.created_at)
        ORDER BY MONTH(u.created_at)
    """, nativeQuery = true)
    List<StatisticProjection> countRegistersLastSixMonths();


    // Estado con cantidad y porcentaje
    @Query(value = """
        SELECT
            CASE 
                WHEN a.status = 'ACTIVE' THEN 'Activos'
                WHEN a.status = 'INACTIVE' THEN 'Inactivos'
                ELSE 'Otros'
            END AS label,

            COUNT(a.id) AS quantity,

            ROUND(
                (COUNT(a.id) * 100.0 / (SELECT COUNT(*) FROM administrator)),
                2
            ) AS percentage

        FROM administrator a
        GROUP BY a.status
    """, nativeQuery = true)
    List<PercentageStatisticProjection> getStatusStatistics();
}
