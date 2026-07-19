package com.san_andres.backend.auth.domain.port.input;

import com.san_andres.backend.auth.domain.model.Session;

import com.san_andres.backend.auth.infrastructure.adapter.output.persistence.projection.ActiveSessionProjection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface SessionUseCase {

    Session createToken(HttpServletRequest request, Authentication authentication);
    Session logout(Long userId);
    Page<ActiveSessionProjection> findActiveSessions(String search, Pageable pageable);

}
