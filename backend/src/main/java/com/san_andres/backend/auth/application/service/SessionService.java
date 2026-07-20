package com.san_andres.backend.auth.application.service;

import com.san_andres.backend.shared.constants.StatusConstants;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.auth.domain.model.Session;
import com.san_andres.backend.shared.web.BrowserResolver;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.auth.domain.port.repository.SessionRepositoryPort;
import com.san_andres.backend.users.domain.port.repository.UserRepositoryPort;
import com.san_andres.backend.auth.domain.port.usecase.SessionUseCase;
import com.san_andres.backend.auth.infrastructure.persistence.projection.ActiveSessionProjection;
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
    private final BrowserResolver browserResolver;

    @Override
    public Session createSession(HttpServletRequest request, Authentication authentication) {
        User user = findUser(authentication.getName());
        Session session = buildSession(user, request);
        return sessionRepositoryPort.save(session);
    }

    @Override
    public Session logout(Long userId) {
        Session session = findActiveSession(userId);
        closeSession(session);
        return sessionRepositoryPort.save(session);
    }

    private void closeSession(Session session) {
        session.setLogoutAt(LocalDateTime.now());
        session.setIsActive(StatusConstants.INACTIVE);
    }

    private Session findActiveSession(Long userId) {
        return sessionRepositoryPort.findActiveByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró ninguna sesión activa."));
    }

    @Override
    public Page<ActiveSessionProjection> findActiveSessions(String search, Pageable pageable) {
        return sessionRepositoryPort.findActiveSessions(search, pageable);
    }

    private User findUser(String login) {
        return userRepositoryPort.findByEmail(login)
                .or(() -> userRepositoryPort.findByUsername(login))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));
    }

    private Session buildSession(User user, HttpServletRequest request) {
        return Session.builder()
                .loginAt(LocalDateTime.now())
                .isActive(StatusConstants.ACTIVE)
                .ipAddress(request.getRemoteAddr())
                .userAgent(browserResolver.resolve(request))
                .user(user)
                .build();
    }
}