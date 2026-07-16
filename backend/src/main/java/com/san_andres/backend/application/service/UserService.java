package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.auth.PasswordRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.DuplicateResourceException;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import com.san_andres.backend.domain.port.usecases.RoleUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepositoryPort repositoryPort;
    private final RoleUseCase roleUseCase;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return repositoryPort.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
    }

    @Override
    public User findById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<User> findByStatus(UserStatus userStatus) {
        return repositoryPort.findByStatus(userStatus);
    }

    @Override
    public List<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return repositoryPort.findByEmailAndStatus(email, userStatus);
    }

    @Override
    public User save( String email, String username ,String password, String role) {

        if (repositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("The email is already registered");
        }

        if (repositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("The username is already registered");
        }

        Role roleEntity = roleUseCase.findByName(role);
        User user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .roles(Collections.singletonList(roleEntity))
                .build();

        return repositoryPort.save(user);
    }

    @Override
    public User update(Long id, String email,String username ,String password, String role) {
        User existingUser = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!existingUser.getEmail().equals(email) && repositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("The email is already registered");
        }

        if (!existingUser.getUsername().equals(username) && repositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("The username is already registered");
        }

        existingUser.setUsername(username);
        existingUser.setEmail(email);

        if (password != null && !password.isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }

        Role roleEntity = roleUseCase.findByName(role);
        existingUser.setRoles(Collections.singletonList(roleEntity));

        existingUser.setUpdatedAt(LocalDateTime.now());

        return repositoryPort.save(existingUser);
    }

    @Override
    public User activateUser(Long id) {

        User existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario No Encontrado"));

        existing.setStatus("ACTIVE");
        return repositoryPort.save(existing);
    }

    @Override
    public User deactivateUser(Long id) {
        User existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario No Encontrado"));

        existing.setStatus("INACTIVE");
        return repositoryPort.save(existing);
    }

    @Override
    public User blockedUser(Long id) {
        User existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario No Encontrado"));

        existing.setStatus("BLOCKED");
        return repositoryPort.save(existing);
    }

    @Override
    public User changePassword(Long userId, PasswordRequest request) {

        User user = repositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return repositoryPort.save(user);
    }
}