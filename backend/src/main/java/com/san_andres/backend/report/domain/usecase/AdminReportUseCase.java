package com.san_andres.backend.report.domain.usecase;

import com.san_andres.backend.report.application.dto.request.AdminReportRequest;

import java.util.List;
import java.util.Map;

public interface AdminReportUseCase {

    byte[] generatePdf(AdminReportRequest request);

    byte[] generatePdfId (Long id);

    byte[] generateExcel(AdminReportRequest request);

    List<Map<String, Object>> findForReport(AdminReportRequest request);

}