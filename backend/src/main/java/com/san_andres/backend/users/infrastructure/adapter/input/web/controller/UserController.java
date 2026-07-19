package com.san_andres.backend.users.infrastructure.adapter.input.web.controller;

import com.san_andres.backend.users.application.dto.request.UserRequest;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.users.domain.port.input.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserUseCase userUseCase;

    @Operation(summary = "Create a new position")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userUseCase.save(userRequest.getEmail(), userRequest.getUsername(), "", ""));
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(
                userUseCase.update(id, userRequest.getEmail(), userRequest.getUsername(), null, userRequest.getRole()));
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userUseCase.findById(id));
    }
}
