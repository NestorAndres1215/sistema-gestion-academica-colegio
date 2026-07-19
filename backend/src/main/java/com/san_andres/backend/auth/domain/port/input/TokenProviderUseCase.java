package com.san_andres.backend.auth.domain.port.input;

import com.san_andres.backend.users.domain.model.User;

public interface TokenProviderUseCase {

    String generateToken(User user);

    String extractUserId(String token);

    boolean validateToken(String token, String userId);

}
