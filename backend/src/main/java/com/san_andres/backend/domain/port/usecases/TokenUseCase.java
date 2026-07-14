package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.infrastructure.persistence.projection.TokenStatusProjection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TokenUseCase {

    Token save (HttpServletRequest request, Authentication authentication);

    List<TokenStatusProjection> findActiveStatusByUserId(Long userId);

    void logout(Long userId);

}
