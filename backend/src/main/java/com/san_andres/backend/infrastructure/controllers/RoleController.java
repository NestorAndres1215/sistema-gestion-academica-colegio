package com.san_andres.backend.infrastructure.controllers;

import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.port.usecases.RoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
@Tag(name = "Role")
public class RoleController {

    private final RoleUseCase roleUseCase;

    @Operation(summary = "Get all roles")
    @GetMapping
    public ResponseEntity<Page<Role>> getByStatus(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(roleUseCase.getAll(search, pageable));

    }

    @Operation(summary = "Get role by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleUseCase.findById(id));
    }

    @Operation(summary = "Get role by name")
    @GetMapping("/name/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name) {
        return ResponseEntity.ok(roleUseCase.findByName(name));
    }
}