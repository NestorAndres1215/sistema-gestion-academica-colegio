package com.san_andres.backend.report.domain.output;

import com.san_andres.backend.report.infrastructure.adapter.output.persistence.projection.AdministratorReportProjection;

import java.util.List;

public interface AdminReportPort {

    List<AdministratorReportProjection> findForReport(String status);
    List<AdministratorReportProjection>findForReportId(Long id);


}