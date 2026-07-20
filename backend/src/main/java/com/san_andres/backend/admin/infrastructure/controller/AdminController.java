package com.san_andres.backend.admin.infrastructure.controller;

import com.san_andres.backend.admin.application.dto.request.AdminRequest;
import com.san_andres.backend.admin.application.dto.response.AdminResponse;
import com.san_andres.backend.report.application.dto.reponse.ImportResult;
import com.san_andres.backend.admin.domain.model.Admin;
import com.san_andres.backend.admin.domain.port.usecase.AdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "Administrator")
public class AdminController {

    private final AdminUseCase adminUseCase;

    @Operation(summary = "Get administrator by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Admin> findById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.findById(id));
    }

    @Operation(summary = "Create administrator")
    @PostMapping
    public ResponseEntity<Admin> save(@Valid @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminUseCase.save(request));
    }

    @Operation(summary = "Update administrator by ID")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Admin> update(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @Valid @RequestPart("admin") AdminRequest request) {

        return ResponseEntity.ok(adminUseCase.update(id, file, request));
    }

    @Operation(summary = "Get all administrators")
    @GetMapping
    public ResponseEntity<Page<AdminResponse>> getByStatus(
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminUseCase.getByStatus(status, search, pageable));

    }

    @Operation(summary = "Deactivate admin")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Admin> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.deactivate(id));
    }

    @Operation(summary = "Activate admin")
    @PutMapping("/activate/{id}")
    public ResponseEntity<Admin> activate(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.activate(id));
    }

    @Operation(summary = "Activate admin")
    @PutMapping("/blocked/{id}")
    public ResponseEntity<Admin> blocked(@PathVariable Long id) {
        return ResponseEntity.ok(adminUseCase.blocked(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<AdminResponse>> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminUseCase.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdminResponse>> search(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(adminUseCase.search(search));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResult> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(adminUseCase.importExcel(file));
    }
}
