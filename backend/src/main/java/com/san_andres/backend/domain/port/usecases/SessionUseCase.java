package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface SessionUseCase {

    Session createToken(HttpServletRequest request, Authentication authentication);
    Session logout(Long userId);
}
