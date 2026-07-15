package com.san_andres.backend.domain.port.usecases;


import com.san_andres.backend.application.dto.auth.LoginRequest;
import com.san_andres.backend.application.dto.auth.TokenResponse;
import com.san_andres.backend.application.dto.auth.UserResponse;
import com.san_andres.backend.domain.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthUseCase {

    UserResponse currentUser(Authentication authentication);

    User authenticate(LoginRequest request);

    String generateToken(User user);

    TokenResponse login(         LoginRequest request,
                                 HttpServletRequest httpRequest);
}