package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.port.repositories.SessionRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import com.san_andres.backend.infrastructure.persistence.mapper.SessionMapper;
import com.san_andres.backend.infrastructure.persistence.projection.ActiveSessionProjection;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionRepositoryPort {

    private final JpaSessionRepository repository;
    private final SessionMapper mapper;

    @Override
    public Session save(Session session) {
        SessionEntity entity = mapper.toEntity(session);
        SessionEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Session> findActiveByUserId(Long userId) {
        return repository.findActiveByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Session> findById(Long id) {
        return  repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<ActiveSessionProjection> findActiveSessions(String search, Pageable pageable) {
        return repository.findActiveSessions(search,pageable);
    }
}
