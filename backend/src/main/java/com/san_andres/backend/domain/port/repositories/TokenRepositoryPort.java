package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.infrastructure.persistence.projection.TokenStatusProjection;

import java.util.List;

public interface TokenRepositoryPort {

    Token save (Token token);
    List<TokenStatusProjection> findActiveStatusByUserId(Long userId);

    void deleteBySessionId(Long sessionId);

}
