package com.san_andres.backend.report.infrastructure.adapter.input.web.controller;

import com.san_andres.backend.report.application.dto.request.TemplateRequest;
import com.san_andres.backend.report.domain.input.ExcelUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
@Tag(name = "Excel")
public class ExcelController {

    private final ExcelUseCase excelUseCase;

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadTemplate(@RequestBody TemplateRequest request) {
        return ResponseEntity.ok(excelUseCase.downloadTemplate(request));
    }
}
