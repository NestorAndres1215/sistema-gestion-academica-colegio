package com.san_andres.backend.infrastructure.controllers;

import com.san_andres.backend.application.dto.admin.AdminReportRequest;
import com.san_andres.backend.domain.port.usecases.AdminReportUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Reports")
public class AdminReportController {

    private final AdminReportUseCase useCase;

    @PostMapping
    public ResponseEntity<byte[]> pdf(@RequestBody AdminReportRequest request){

        byte[] file = useCase.generatePdf(request);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=administrators.pdf"
                )
                .contentType( MediaType.APPLICATION_PDF )
                .body(file);

    }

    @PostMapping("/{id}")
    public ResponseEntity<byte[]> pdfId(@PathVariable Long id){

        byte[] file = useCase.generatePdfId(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=administrators.pdf"
                )
                .contentType( MediaType.APPLICATION_PDF )
                .body(file);

    }

}
