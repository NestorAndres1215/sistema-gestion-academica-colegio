package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.excel.TemplateRequest;
import com.san_andres.backend.domain.port.repositories.ExcelGeneratorPort;
import com.san_andres.backend.domain.port.usecases.ExcelUseCase;
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
