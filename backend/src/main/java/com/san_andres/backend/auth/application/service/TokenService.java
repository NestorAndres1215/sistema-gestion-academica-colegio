package com.san_andres.backend.auth.application.service;

import com.san_andres.backend.auth.domain.model.Session;
import com.san_andres.backend.auth.domain.model.Token;
import com.san_andres.backend.auth.domain.port.repository.TokenRepositoryPort;
import com.san_andres.backend.auth.domain.port.usecase.SessionUseCase;
import com.san_andres.backend.auth.domain.port.usecase.TokenUseCase;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements TokenUseCase {

    private final SessionUseCase sessionUseCase;
    private final TokenRepositoryPort tokenRepositoryPort;

    @Override
    public Token save(String jwt, HttpServletRequest request, Authentication authentication) {
        Session session = sessionUseCase.createSession(request, authentication);
        Token token = buildToken(jwt, session);
        return tokenRepositoryPort.save(token);
    }

    @Override
    public void logout(Long userId) {
        Session session=sessionUseCase.logout( userId);
        tokenRepositoryPort.deleteBySessionId(session.getId());
    }

    @Override
    public List<TokenStatusProjection> findActiveStatusByUserId(Long userId) {
        return tokenRepositoryPort.findActiveStatusByUserId(userId);
    }

    private Token buildToken(String jwt, Session session) {

        LocalDateTime now = LocalDateTime.now();

        return Token.builder()
                .token(jwt)
                .session(session)
                .createdAt(now)
                .expiresAt(now.plusDays(7))
                .build();
    }

}
