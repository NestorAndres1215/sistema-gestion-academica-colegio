package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.domain.models.User;

public interface TokenProviderPort {

    String generateToken(User user);

    String extractUserId(String token);

    boolean validateToken(String token, String userId);

}
