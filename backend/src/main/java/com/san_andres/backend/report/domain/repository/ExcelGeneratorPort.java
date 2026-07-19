package com.san_andres.backend.report.domain.repository;

import com.san_andres.backend.report.application.dto.request.TemplateRequest;

import java.util.List;

public interface ExcelGeneratorPort {

    byte[] generateList(String title, List<String> headers, List<List<String>> rows);

    byte[] generateTemplate(TemplateRequest request);

}