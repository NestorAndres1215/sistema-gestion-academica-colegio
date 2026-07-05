package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Session;

import java.util.Optional;


public interface SessionRepositoryPort {

    Session save (Session session);
    Optional<Session> findActiveByUserId(Long userId);
}