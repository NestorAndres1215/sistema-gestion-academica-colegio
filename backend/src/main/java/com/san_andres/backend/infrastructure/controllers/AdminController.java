package com.san_andres.backend.infrastructure.controllers;

import com.san_andres.backend.application.dto.admin.AdminRequest;
import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.domain.port.usecases.AdminUseCase;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Admin> save(
            @RequestPart("file") MultipartFile file,
            @Valid @RequestPart("user") AdminRequest request) {
        return ResponseEntity.ok(adminUseCase.save(file,request));
    }

    @Operation(summary = "Update administrator by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Admin> update(@PathVariable Long id, @Valid @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminUseCase.update(id, request));
    }

    @Operation(summary = "Get all administrators")
    @GetMapping
    public ResponseEntity<Page<AdminResponse>> getByStatus(
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search
    ) {

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

}
