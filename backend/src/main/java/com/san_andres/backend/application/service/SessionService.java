package com.san_andres.backend.application.service;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.SessionRepositoryPort;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import com.san_andres.backend.domain.port.usecases.SessionUseCase;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService implements SessionUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SessionRepositoryPort sessionRepositoryPort;


    @Override
    public Session createToken(HttpServletRequest request, Authentication authentication) {
        String username = authentication.getName();

        User user = userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String ua = request.getHeader("User-Agent");

        String browser = "Unknown";

        if (ua.contains("Chrome") && !ua.contains("Edg")) {
            browser = "Chrome";
        } else if (ua.contains("Edg")) {
            browser = "Edge";
        } else if (ua.contains("Firefox")) {
            browser = "Firefox";
        } else if (ua.contains("Safari") && !ua.contains("Chrome")) {
            browser = "Safari";
        }
        Session session = Session.builder()
                .id(UUID.randomUUID().toString())
                .loginAt(LocalDateTime.now())
                .isActive(UserStatus.ACTIVE)
                .ipAddress(request.getRemoteAddr())
                .userAgent(browser)
                .user(user)
                .build();

        return sessionRepositoryPort.save(session);
    }

    @Override
    public Session logout(String userId) {

        Session session = sessionRepositoryPort.findActiveByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Active session not found"));

        session.setLogoutAt(LocalDateTime.now());
        session.setIsActive(UserStatus.INACTIVE);

        return sessionRepositoryPort.save(session);
    }
}
