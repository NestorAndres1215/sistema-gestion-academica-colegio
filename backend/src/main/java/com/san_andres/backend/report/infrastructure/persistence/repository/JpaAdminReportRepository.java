package com.san_andres.backend.report.infrastructure.persistence.repository;

import com.san_andres.backend.admin.infrastructure.persistence.entity.AdminEntity;
import com.san_andres.backend.report.infrastructure.persistence.projection.AdministratorReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaAdminReportRepository extends JpaRepository<AdminEntity, Long> {

    @Query("""
                SELECT
                    u.email AS email,
                       u.username AS username,
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
    List<AdministratorReportProjection> findForReport(@Param("status") String status);

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
    List<AdministratorReportProjection> findForReportId(@Param("id") Long id);
}
