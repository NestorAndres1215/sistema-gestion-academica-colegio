package com.san_andres.backend.dashboard.infrastructure.persistence.repository;

import com.san_andres.backend.admin.infrastructure.persistence.entity.AdminEntity;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAdminDashboardRepository extends JpaRepository<AdminEntity, Long> {

    // Total administradores
    @Query("""
                SELECT
                    'Total administradores' AS label,
                    COUNT(a) AS quantity
                FROM AdminEntity a
            """)
    DashboardProjection countAdministrators();

    // Administradores activos
    @Query("""
                SELECT
                    'Administradores activos' AS label,
                    COUNT(a) AS quantity
                FROM AdminEntity a
                WHERE a.status = 'ACTIVE'
            """)
    DashboardProjection countActiveAdministrators();

    // Administradores inactivos
    @Query("""
                SELECT
                    'Administradores inactivos' AS label,
                    COUNT(a) AS quantity
                FROM AdminEntity a
                WHERE a.status = 'INACTIVE'
            """)
    DashboardProjection countInactiveAdministrators();
    
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
    DashboardProjection countRegisteredLastMonth();

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
    List<DashboardProjection> countByGender();

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
    List<DashboardProjection> countRegistersLastSixMonths();

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
    List<DashboardProjection> getStatusStatistics();
}
