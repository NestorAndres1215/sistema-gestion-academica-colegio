package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Token;

public interface TokenRepositoryPort {
    Token save (Token token);

    void deleteBySessionId(Long sessionId);
}
