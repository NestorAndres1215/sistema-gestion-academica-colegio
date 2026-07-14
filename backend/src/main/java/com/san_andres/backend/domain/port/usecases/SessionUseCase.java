package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Session;

import com.san_andres.backend.infrastructure.persistence.projection.ActiveSessionProjection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface SessionUseCase {

    Session createToken(HttpServletRequest request, Authentication authentication);
    Session logout(Long userId);
    Page<ActiveSessionProjection> findActiveSessions(String search, Pageable pageable);

}
