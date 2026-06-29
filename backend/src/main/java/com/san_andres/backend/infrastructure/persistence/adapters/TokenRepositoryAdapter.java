package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.domain.port.repositories.TokenRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import com.san_andres.backend.infrastructure.persistence.entities.TokenEntity;
import com.san_andres.backend.infrastructure.persistence.mapper.SessionMapper;

import com.san_andres.backend.infrastructure.persistence.mapper.TokenMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    private final JpaTokenRepository repository;
    private final TokenMapper mapper;

    @Override
    public Token save(Token token) {
        TokenEntity entity = mapper.toEntity(token);
        TokenEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        repository.deleteBySessionId(sessionId);
    }
}
