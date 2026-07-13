package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.admin.AdminReportRequest;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;

import java.util.List;
import java.util.Map;

public interface AdminReportUseCase {

    byte[] generatePdf(AdminReportRequest request);
    byte[] generatePdfId (Long id);
    byte[] generateExcel(AdminReportRequest request);
    List<Map<String, Object>> findForReport(AdminReportRequest request);
}