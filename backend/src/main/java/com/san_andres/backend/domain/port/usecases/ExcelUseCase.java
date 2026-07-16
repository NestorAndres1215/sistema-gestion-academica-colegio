package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.excel.TemplateRequest;

import java.util.List;

public interface ExcelUseCase {
    byte[] downloadTemplate(TemplateRequest request);
}
