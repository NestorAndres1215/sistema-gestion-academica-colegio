package com.san_andres.backend.infrastructure.controllers;

import com.san_andres.backend.application.dto.userStory.UserStoryRequest;
import com.san_andres.backend.application.dto.userStory.UserStoryResponse;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.UserStory;
import com.san_andres.backend.domain.port.usecases.UserStoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-story")
@Tag(name = "User Story")
public class UserStoryController {

    private final UserStoryUseCase userStoryUseCase;

    @Operation(summary = "Get user stories with filters")
    @GetMapping
    public ResponseEntity<Page<UserStoryResponse>> findWithFilters(
            @RequestParam String email,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
    Sort sortOrder = Sort.by(
            sort.equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
            "createdAt"
    );

    Pageable pageable = PageRequest.of(page, size, sortOrder);

    return ResponseEntity.ok(userStoryUseCase.findWithFilters(email, status, action, dateFrom, dateTo, pageable));
    }

    @Operation(summary = "Register a new user story")
    @PostMapping
    public ResponseEntity<UserStory> save(@RequestBody UserStoryRequest request) {
        return ResponseEntity.ok(userStoryUseCase.save(request));
    }

    @Operation(summary = "Activate user story")
    @PutMapping("/{id}/activate")
    public ResponseEntity<UserStory> activate(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryUseCase.activate(id));
    }

    @Operation(summary = "Deactivate user story")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserStory> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryUseCase.deactivate(id));
    }


}