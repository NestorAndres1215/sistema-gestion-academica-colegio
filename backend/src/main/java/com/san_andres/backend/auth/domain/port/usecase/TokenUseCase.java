package com.san_andres.backend.auth.domain.port.usecase;

import com.san_andres.backend.auth.domain.model.Token;
import com.san_andres.backend.auth.infrastructure.persistence.projection.TokenStatusProjection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface TokenUseCase {

    Token save(String jwt, HttpServletRequest request, Authentication authentication);

    List<TokenStatusProjection> findActiveStatusByUserId(Long userId);

    void logout(Long userId);

}
