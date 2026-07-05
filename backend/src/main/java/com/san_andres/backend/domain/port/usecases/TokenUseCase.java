package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenUseCase {

    Token save (HttpServletRequest request, Authentication authentication);
    void logout(Long userId);
}
