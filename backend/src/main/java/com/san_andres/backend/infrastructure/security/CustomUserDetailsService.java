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



    // 🔐 SOLO LOGIN
    @Override
    public UserDetails loadUserByUsername(String login) {

        User user = userRepository.findByUsername(login)
                .orElseGet(() -> userRepository.findByEmail(login)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Usuario no encontrado")));

        return new CustomUserDetails(user);
    }

    // 🎟 JWT (NUEVO)
    public UserDetails loadUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(user);
    }
}