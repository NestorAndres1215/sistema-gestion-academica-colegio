package com.san_andres.backend.report.domain.input;

import com.san_andres.backend.report.application.dto.request.TemplateRequest;

public interface ExcelUseCase {
    byte[] downloadTemplate(TemplateRequest request);
}
