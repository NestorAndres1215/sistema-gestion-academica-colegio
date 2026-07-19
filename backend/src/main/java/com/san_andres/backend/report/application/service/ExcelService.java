package com.san_andres.backend.report.application.service;

import com.san_andres.backend.report.application.dto.request.TemplateRequest;
import com.san_andres.backend.report.domain.output.ExcelGeneratorPort;
import com.san_andres.backend.report.domain.input.ExcelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcelService implements ExcelUseCase {

    private final ExcelGeneratorPort excelGeneratorPort;

    @Override
    public byte[] downloadTemplate(TemplateRequest request) {
        return excelGeneratorPort.generateTemplate(request);
    }

}
