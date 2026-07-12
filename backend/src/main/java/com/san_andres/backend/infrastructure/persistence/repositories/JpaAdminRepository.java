package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.AdminEntity;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;
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
    @Query(value = """
        SELECT 
            g.label AS label,
            COUNT(a.id) AS quantity
        FROM (
            SELECT 'Masculino' AS label, 'MALE' AS gender
            UNION ALL
            SELECT 'Femenino' AS label, 'FEMALE' AS gender
            UNION ALL
            SELECT 'Otro' AS label, 'OTHER' AS gender
        ) g
        LEFT JOIN administrator a ON a.gender = g.gender
        GROUP BY g.label
    """, nativeQuery = true)
    List<StatisticProjection> countByGender();

    // Registros últimos 6 meses
    @Query(value = """
        SELECT
            DATE_FORMAT(m.month_date, '%M') AS label,
            COALESCE(COUNT(a.id), 0) AS quantity
        FROM (
            SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH), '%Y-%m-01') AS month_date
            UNION ALL
            SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 MONTH), '%Y-%m-01')
            UNION ALL
            SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 MONTH), '%Y-%m-01')
            UNION ALL
            SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01')
            UNION ALL
            SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01')
            UNION ALL
            SELECT DATE_FORMAT(CURDATE(), '%Y-%m-01')
        ) m
        LEFT JOIN users u
            ON DATE_FORMAT(u.created_at, '%Y-%m') = DATE_FORMAT(m.month_date, '%Y-%m')
        LEFT JOIN administrator a
            ON a.user_id = u.id
        GROUP BY m.month_date
        ORDER BY m.month_date
    """, nativeQuery = true)
    List<StatisticProjection> countRegistersLastSixMonths();


    // Estado con cantidad y porcentaje
    @Query(value = """
        SELECT
            s.label,
            COALESCE(COUNT(a.id), 0) AS quantity
        FROM (
            SELECT 'ACTIVE' AS status, 'Activos' AS label
            UNION ALL
            SELECT 'INACTIVE', 'Inactivos'
        ) s
        LEFT JOIN administrator a
            ON a.status = s.status
        GROUP BY s.status, s.label
        ORDER BY FIELD(s.status, 'ACTIVE', 'INACTIVE')
    """, nativeQuery = true)
    List<StatisticProjection> getStatusStatistics();


    @Query("""
        SELECT
            u.email AS email,
            a.firstName AS firstName,
            a.middleName AS middleName,
            a.paternalLastName AS paternalLastName,
            a.maternalLastName AS maternalLastName,
            a.phone AS phone,
            a.dni AS dni,
            a.gender AS gender,
            a.status AS status
        FROM AdminEntity a
        JOIN a.userEntity u
        WHERE (:status IS NULL OR :status = '' OR a.status = :status)
    """)
    List<AdministratorReportProjection> findForReport( @Param("status") String status );

    @Query("""
        SELECT
            u.email AS email,
            a.firstName AS firstName,
            a.middleName AS middleName,
            a.paternalLastName AS paternalLastName,
            a.maternalLastName AS maternalLastName,
            a.phone AS phone,
            a.dni AS dni,
            a.gender AS gender,
            a.status AS status
        FROM AdminEntity a
        JOIN a.userEntity u
        WHERE (:id IS NULL OR a.id = :id)
    """)
    List<AdministratorReportProjection> findForReportId(
            @Param("id") Long id
    );
}
