package com.san_andres.backend.auth.domain.port.repository;

import com.san_andres.backend.auth.domain.model.Token;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;

import java.util.List;

public interface TokenRepositoryPort {

    Token save (Token token);
    List<TokenStatusProjection> findActiveStatusByUserId(Long userId);
    Token findByToken(String token);
    void deleteBySessionId(Long sessionId);

}
