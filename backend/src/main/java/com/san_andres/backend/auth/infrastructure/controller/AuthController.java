package com.san_andres.backend.auth.infrastructure.controller;

import com.san_andres.backend.auth.application.dto.request.LoginRequest;
import com.san_andres.backend.auth.application.dto.request.PasswordRequest;
import com.san_andres.backend.auth.application.dto.response.TokenResponse;
import com.san_andres.backend.users.application.dto.response.UserResponse;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.auth.domain.port.usecase.AuthUseCase;
import com.san_andres.backend.auth.domain.port.usecase.TokenUseCase;
import com.san_andres.backend.users.domain.port.usecase.UserUseCase;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private final TokenUseCase tokenUseCase;

    @Operation(summary = "Generate authentication token")
    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                authUseCase.login(request, httpRequest));
    }

    @Operation(summary = "Get currently authenticated user")
    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authUseCase.currentUser(authentication));
    }

    @Operation(summary = "Change user password")
    @PostMapping("/{id}/change-password")
    public ResponseEntity<User> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordRequest passwordRequest) {

        return ResponseEntity.ok(userUseCase.changePassword(id, passwordRequest));

    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId) {
        tokenUseCase.logout(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/session/{userId}/status")
    public ResponseEntity<List<TokenStatusProjection>> findStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(tokenUseCase.findActiveStatusByUserId(userId));
    }

}