package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.application.dto.excel.TemplateRequest;

import java.util.List;

public interface ExcelGeneratorPort {

    byte[] generateList(String title, List<String> headers, List<List<String>> rows);

    byte[] generateTemplate(TemplateRequest request);
}