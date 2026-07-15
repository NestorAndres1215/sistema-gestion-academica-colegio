package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.auth.LoginRequest;
import com.san_andres.backend.application.dto.auth.TokenResponse;
import com.san_andres.backend.application.dto.auth.UserResponse;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.TokenProviderPort;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import com.san_andres.backend.domain.port.usecases.AuthUseCase;
import com.san_andres.backend.domain.port.usecases.SessionUseCase;
import com.san_andres.backend.domain.port.usecases.TokenUseCase;
import com.san_andres.backend.infrastructure.security.CustomUserDetails;
import com.san_andres.backend.infrastructure.security.JwtAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort repositoryPort;
    private final TokenUseCase tokenUseCase;
    private final JwtAdapter jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenProviderPort tokenProviderPort;
private final SessionUseCase sessionUseCase;

    @Override
    public UserResponse currentUser(Authentication authentication) {

        String login = authentication.getName();

        User user = repositoryPort.findByUsername(login)
                .orElseGet(() -> repositoryPort.findByEmail(login)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Usuario No Encontrado")));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(
                        user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse(null)
                )
                .build();
    }


    @Override
    public User authenticate(LoginRequest request) {
        return repositoryPort.findByUsername(request.getLogin())
                .orElseGet(() -> repositoryPort.findByEmail(request.getLogin())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Usuario No Encontrado")));
    }

    @Override
    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    @Override
    public TokenResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest
    ) {


        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.getLogin(),
                                request.getPassword()
                        )
                );


        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();


        User user =
                userDetails.getUser();



        String jwt =
                tokenProviderPort.generateToken(user);



        // CREA SESSION + GUARDA TOKEN
        tokenUseCase.save(
                jwt,
                httpRequest,
                authentication
        );


        return TokenResponse.builder()
                .token(jwt)
                .build();

    }
}