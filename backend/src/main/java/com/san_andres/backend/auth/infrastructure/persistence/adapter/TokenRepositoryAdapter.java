package com.san_andres.backend.auth.infrastructure.persistence.adapter;

import com.san_andres.backend.auth.domain.model.Token;
import com.san_andres.backend.auth.domain.port.repository.TokenRepositoryPort;
import com.san_andres.backend.auth.infrastructure.persistence.entity.TokenEntity;
import com.san_andres.backend.auth.infrastructure.persistence.mapper.TokenMapper;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;
import com.san_andres.backend.auth.infrastructure.persistence.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

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
    public void deleteBySessionId(Long sessionId) {
        repository.deleteBySession_Id(sessionId);
    }

    @Override
    public List<TokenStatusProjection> findActiveStatusByUserId(Long userId) {
        return repository.findActiveTokenStatusByUserId(userId);
    }

    @Override
    public Token findByToken(String token) {
        TokenEntity entity = repository.findByToken(token).orElse(null);
        return mapper.toDomain(entity);
    }
}
