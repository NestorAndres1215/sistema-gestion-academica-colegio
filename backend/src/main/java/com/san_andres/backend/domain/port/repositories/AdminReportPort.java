package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;

import java.util.List;

public interface AdminReportPort {

    List<AdministratorReportProjection> findForReport(String status);
    List<AdministratorReportProjection>findForReportId(Long id);


}