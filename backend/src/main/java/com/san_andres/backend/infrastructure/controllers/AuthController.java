package com.san_andres.backend.infrastructure.controllers;

import com.san_andres.backend.application.dto.auth.LoginRequest;
import com.san_andres.backend.application.dto.auth.PasswordRequest;
import com.san_andres.backend.application.dto.auth.TokenResponse;
import com.san_andres.backend.application.dto.auth.UserResponse;
import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.usecases.AuthUseCase;
import com.san_andres.backend.domain.port.usecases.TokenUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authUseCase.login(request));
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


    @PostMapping("/generate-session")
    public ResponseEntity<Token> createSession(HttpServletRequest request, Authentication authentication) {
        return ResponseEntity.ok(tokenUseCase.save(request, authentication));
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId) {

        tokenUseCase.logout(userId);

        return ResponseEntity.ok().build();
    }

}