package com.san_andres.backend.application.service;

import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.domain.port.repositories.TokenRepositoryPort;
import com.san_andres.backend.domain.port.usecases.SessionUseCase;
import com.san_andres.backend.domain.port.usecases.TokenUseCase;
import com.san_andres.backend.infrastructure.persistence.projection.TokenStatusProjection;
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

        Session session = sessionUseCase.createToken(request, authentication);

        LocalDateTime now = LocalDateTime.now();

        Token token = Token.builder()
                .token(jwt)
                .session(session)
                .createdAt(now)
                .expiresAt(now.plusDays(7))
                .build();

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

}
