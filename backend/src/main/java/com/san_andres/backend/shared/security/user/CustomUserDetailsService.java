package com.san_andres.backend.shared.security.user;

import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.users.domain.port.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = findUser(login);
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(user);
    }

    private User findUser(String login) {
        return userRepositoryPort.findByUsername(login)
                .orElseGet(() -> userRepositoryPort.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado")));
    }
}