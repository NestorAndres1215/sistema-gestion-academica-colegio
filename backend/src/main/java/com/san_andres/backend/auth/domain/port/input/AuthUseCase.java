package com.san_andres.backend.auth.domain.port.input;


import com.san_andres.backend.auth.application.dto.request.LoginRequest;
import com.san_andres.backend.auth.application.dto.response.TokenResponse;
import com.san_andres.backend.users.application.dto.response.UserResponse;
import com.san_andres.backend.users.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthUseCase {

    UserResponse currentUser(Authentication authentication);

    User authenticate(LoginRequest request);

    String generateToken(User user);

    TokenResponse login(LoginRequest request,
                        HttpServletRequest httpRequest);
}