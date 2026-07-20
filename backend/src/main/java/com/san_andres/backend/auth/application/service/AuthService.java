package com.san_andres.backend.auth.application.service;

import com.san_andres.backend.auth.application.dto.request.LoginRequest;
import com.san_andres.backend.auth.application.dto.response.TokenResponse;
import com.san_andres.backend.users.application.dto.response.UserResponse;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.role.domain.model.Role;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.shared.security.port.TokenProviderPort;
import com.san_andres.backend.users.domain.port.repository.UserRepositoryPort;
import com.san_andres.backend.auth.domain.port.usecase.AuthUseCase;
import com.san_andres.backend.auth.domain.port.usecase.TokenUseCase;
import com.san_andres.backend.shared.security.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

        private final UserRepositoryPort repositoryPort;
        private final TokenUseCase tokenUseCase;
        private final AuthenticationManager authenticationManager;
        private final TokenProviderPort tokenProviderPort;

        @Override
        public UserResponse currentUser(Authentication authentication) {

                User user = findUser(authentication.getName());

                return UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .status(user.getStatus())
                        .role(user.getRoles()
                                .stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse(null))
                        .build();
        }

        @Override
        public User authenticate(LoginRequest request) {
                return findUser(request.getLogin());
        }

        @Override
        public String generateToken(User user) {
                return tokenProviderPort.generateToken(user);
        }

        @Override
        public TokenResponse login(LoginRequest request, HttpServletRequest httpRequest) {

                Authentication authentication = authenticateUser(request);

                User user = extractUser(authentication);

                String jwt = tokenProviderPort.generateToken(user);

                tokenUseCase.save(jwt, httpRequest, authentication);

                return TokenResponse.builder()
                        .token(jwt)
                        .build();

        }

        private Authentication authenticateUser(LoginRequest request) {
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        }

        private User extractUser(Authentication authentication) {
                return ((CustomUserDetails) authentication.getPrincipal())
                        .getUser();
        }

        private User findUser(String login) {
                return repositoryPort.findByUsername(login)
                        .orElseGet(() ->
                                repositoryPort.findByEmail(login)
                                        .orElseThrow(() ->
                                                new ResourceNotFoundException("Usuario no encontrado")));
        }
}