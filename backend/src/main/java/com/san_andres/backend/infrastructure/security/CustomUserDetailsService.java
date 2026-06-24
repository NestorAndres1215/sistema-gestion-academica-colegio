package com.san_andres.backend.infrastructure.security;

import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email));

        return new CustomUserDetails(user);
    }
}