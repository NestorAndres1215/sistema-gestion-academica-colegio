package com.san_andres.backend.report.infrastructure.controller;

import com.san_andres.backend.report.application.dto.request.AdminReportRequest;
import com.san_andres.backend.report.domain.usecase.AdminReportUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Reports")
public class AdminReportController {

        private final AdminReportUseCase useCase;

        @PostMapping("/pdf")
        public ResponseEntity<byte[]> pdf(@RequestBody AdminReportRequest request) {

                byte[] file = useCase.generatePdf(request);

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=administrators.pdf")
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(file);

        }

        @PostMapping("/pdf/{id}")
        public ResponseEntity<byte[]> pdfId(@PathVariable Long id) {

                byte[] file = useCase.generatePdfId(id);

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=administrators.pdf")
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(file);

        }

        @PostMapping("/excel")
        public ResponseEntity<byte[]> generateExcel(@RequestBody AdminReportRequest request) {

                byte[] excel = useCase.generateExcel(request);

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=reporte_administradores.xlsx")
                                .contentType(
                                                MediaType.parseMediaType(
                                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                                .body(excel);
        }

        @PostMapping("/print")
        public ResponseEntity<List<Map<String, Object>>> print(
                        @RequestBody AdminReportRequest request) {

                return ResponseEntity.ok(useCase.findForReport(request));
        }
}
