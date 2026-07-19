package com.san_andres.backend.auth.domain.port.output;

import com.san_andres.backend.auth.domain.model.Session;
import com.san_andres.backend.auth.infrastructure.adapter.output.persistence.projection.ActiveSessionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface SessionRepositoryPort {

    Session save (Session session);
    Optional<Session> findActiveByUserId(Long userId);
    Optional<Session> findById(Long id);
    Page<ActiveSessionProjection> findActiveSessions( String search, Pageable pageable);
}