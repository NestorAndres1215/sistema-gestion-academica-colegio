package com.san_andres.backend.auth.application.service;

import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.auth.domain.model.Session;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.auth.domain.port.output.SessionRepositoryPort;
import com.san_andres.backend.users.domain.port.output.UserRepositoryPort;
import com.san_andres.backend.auth.domain.port.input.SessionUseCase;
import com.san_andres.backend.auth.infrastructure.adapter.output.persistence.projection.ActiveSessionProjection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService implements SessionUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SessionRepositoryPort sessionRepositoryPort;

    @Override
    public Session createToken(HttpServletRequest request, Authentication authentication) {

        String principal = authentication.getName();

        User user = userRepositoryPort.findByEmail(principal)
                .or(() -> userRepositoryPort.findByUsername(principal))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Session session = Session.builder()
                .loginAt(LocalDateTime.now())
                .isActive("ACTIVE")
                .ipAddress(request.getRemoteAddr())
                .userAgent(getBrowser(request))
                .user(user)
                .build();

        return sessionRepositoryPort.save(session);
    }

    private String getBrowser(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown";
        }

        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            return "Chrome";
        }

        if (userAgent.contains("Edg")) {
            return "Edge";
        }

        if (userAgent.contains("Firefox")) {
            return "Firefox";
        }

        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        }

        return "Unknown";
    }

    @Override
    public Session logout(Long userId) {

        Session session = sessionRepositoryPort.findActiveByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ninguna sesión activa."));

        session.setLogoutAt(LocalDateTime.now());
        session.setIsActive("INACTIVE");

        return sessionRepositoryPort.save(session);
    }

    @Override
    public Page<ActiveSessionProjection> findActiveSessions(String search, Pageable pageable) {
        return sessionRepositoryPort.findActiveSessions(search, pageable);
    }
}
