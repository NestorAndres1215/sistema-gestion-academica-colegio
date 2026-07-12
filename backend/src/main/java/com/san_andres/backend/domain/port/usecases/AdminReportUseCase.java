package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.admin.AdminReportRequest;

public interface AdminReportUseCase {

    byte[] generatePdf(AdminReportRequest request);
    byte[] generatePdfId (Long id);
}