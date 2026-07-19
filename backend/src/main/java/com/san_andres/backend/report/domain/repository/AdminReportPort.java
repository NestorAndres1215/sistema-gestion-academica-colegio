package com.san_andres.backend.report.domain.repository;

import com.san_andres.backend.report.infrastructure.persistence.projection.AdministratorReportProjection;

import java.util.List;

public interface AdminReportPort {

    List<AdministratorReportProjection> findForReport(String status);

    List<AdministratorReportProjection>findForReportId(Long id);

}