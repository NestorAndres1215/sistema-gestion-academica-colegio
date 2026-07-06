package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.infrastructure.persistence.entities.TokenEntity;

import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    private final SessionMapper sessionMapper;

    public TokenMapper(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    public Token toDomain(TokenEntity entity) {
        if (entity == null) return null;

        return Token.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .revokedAt(entity.getRevokedAt())
                .session(
                        entity.getSession() == null ? null :
                                sessionMapper.toDomain(entity.getSession())
                )
                .build();
    }

    public TokenEntity toEntity(Token domain) {
        if (domain == null) return null;

        return TokenEntity.builder()
                .id(domain.getId())
                .token(domain.getToken())
                .createdAt(domain.getCreatedAt())
                .expiresAt(domain.getExpiresAt())
                .revokedAt(domain.getRevokedAt())
                .session(
                        domain.getSession() == null ? null :
                                sessionMapper.toEntity(domain.getSession())
                )
                .build();
    }
}