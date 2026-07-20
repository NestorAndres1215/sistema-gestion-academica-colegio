package com.san_andres.backend.shared.security.port;

import com.san_andres.backend.users.domain.model.User;

public interface TokenProviderPort {

    String generateToken(User user);

    String extractUserId(String token);

    boolean validateToken(String token, String userId);

}
