package com.san_andres.backend.shared.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtAuthenticationService {
    UserDetails authenticate(String token);
}
