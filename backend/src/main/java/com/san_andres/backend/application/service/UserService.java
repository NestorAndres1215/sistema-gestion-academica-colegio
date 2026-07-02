package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.auth.PasswordRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.BadRequestException;
import com.san_andres.backend.domain.exceptions.DuplicateResourceException;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import com.san_andres.backend.domain.port.usecases.RoleUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import com.san_andres.backend.infrastructure.utils.SequenceGenerator;
import jakarta.validation.Valid;
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
    public User findById(String id) {
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
    public User save(String id, String email, String username ,String password, String role) {

        if (repositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("The email is already registered");
        }

        if (repositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("The username is already registered");
        }

        String newCode = SequenceGenerator.generateCode(repositoryPort.findLastCode());
        Role roleEntity = roleUseCase.findByName(role);
        User user = User.builder()
                .id(newCode)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .roles(Collections.singletonList(roleEntity))
                .build();

        return repositoryPort.save(user);
    }

    @Override
    public User update(String id, String email,String username ,String password, String role) {
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
    public User activateUser(String id) {

        User existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator not found"));

        existing.setStatus(UserStatus.ACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public User deactivateUser(String id) {
        User existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator not found"));

        existing.setStatus(UserStatus.INACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public User changePassword(String userId, PasswordRequest request) {

        User user = repositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            throw new BadRequestException("La nueva contraseña debe ser diferente a la actual");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return repositoryPort.save(user);
    }
}