package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import com.san_andres.backend.infrastructure.persistence.projection.ActiveSessionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface SessionRepositoryPort {

    Session save (Session session);
    Optional<Session> findActiveByUserId(Long userId);
    Optional<Session> findById(Long id);
    Page<ActiveSessionProjection> findActiveSessions( String search, Pageable pageable);
}