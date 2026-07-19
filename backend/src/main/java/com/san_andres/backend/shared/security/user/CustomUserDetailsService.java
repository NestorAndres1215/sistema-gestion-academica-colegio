package com.san_andres.backend.shared.security.user;

import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.users.domain.port.output.UserRepositoryPort;
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
    public UserDetails loadUserByUsername(String login) {

        User user = userRepository.findByUsername(login)
                .orElseGet(() -> userRepository.findByEmail(login)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Usuario no encontrado")));

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(user);
    }
}