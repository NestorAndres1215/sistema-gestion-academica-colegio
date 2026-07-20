package com.san_andres.backend.shared.security.service;

import com.san_andres.backend.auth.domain.model.Token;
import com.san_andres.backend.auth.domain.port.repository.TokenRepositoryPort;
import com.san_andres.backend.shared.constants.StatusConstants;
import com.san_andres.backend.shared.security.jwt.JwtAdapter;
import com.san_andres.backend.shared.security.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements  JwtAuthenticationService {

    private final JwtAdapter jwtAdapter;

    private final TokenRepositoryPort tokenRepositoryPort;

    private final CustomUserDetailsService userDetailsService;


    @Override
    public UserDetails authenticate(String token) {

        String userId;

        Token tokenEntity = tokenRepositoryPort.findByToken(token);

        if (tokenEntity == null) {
            throw new BadCredentialsException("Token inválido");
        }

        if (tokenEntity.getSession() == null ||
                !StatusConstants.ACTIVE.equals(
                        tokenEntity.getSession().getIsActive())) {

            throw new BadCredentialsException("Sesión expirada");
        }

        try {
            userId = jwtAdapter.extractUserId(token);
        } catch (Exception e) {
            throw new BadCredentialsException("Token inválido");
        }

        if (!jwtAdapter.validateToken(token, userId)) {
            throw new BadCredentialsException("Token inválido");
        }

        return userDetailsService.loadUserById(Long.valueOf(userId));
    }
}
