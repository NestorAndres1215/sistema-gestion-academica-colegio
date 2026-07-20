package com.san_andres.backend.users.application.service;

import com.san_andres.backend.auth.application.dto.request.PasswordRequest;
import com.san_andres.backend.shared.constants.StatusConstants;
import com.san_andres.backend.shared.exception.BadRequestException;
import com.san_andres.backend.shared.exception.DuplicateResourceException;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.role.domain.model.Role;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.users.domain.port.repository.UserRepositoryPort;
import com.san_andres.backend.role.domain.port.usecase.RoleUseCase;
import com.san_andres.backend.users.domain.port.usecase.UserUseCase;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<User> findByStatus(String status) {
        return repositoryPort.findByStatus(status);
    }

    @Override
    public List<User> findByEmailAndStatus(String email, String status) {
        return repositoryPort.findByEmailAndStatus(email, status);
    }

    @Override
    public User save(String email, String username, String password, String role) {
        validateUserDoesNotExist(email, username);

        Role roleEntity = roleUseCase.findByName(role);
        User user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .status(StatusConstants.ACTIVE)
                .createdAt(LocalDateTime.now())
                .roles(Collections.singletonList(roleEntity))
                .build();

        return repositoryPort.save(user);
    }

    private void validateUserDoesNotExist(String email, String username) {

        if (repositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("The email is already registered");
        }

        if (repositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("The username is already registered");
        }
    }


    @Override
    public User update(Long id, String email, String username, String password, String role) {

        User existingUser = findById(id);

        validateUserUpdate(existingUser, email, username);

        existingUser.setEmail(email);
        existingUser.setUsername(username);

        if (password != null && !password.isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }

        Role roleEntity = roleUseCase.findByName(role);
        existingUser.setRoles(Collections.singletonList(roleEntity));

        existingUser.setUpdatedAt(LocalDateTime.now());

        return repositoryPort.save(existingUser);
    }

    private void validateUserUpdate(User existingUser, String email, String username) {

        if (!existingUser.getEmail().equals(email)
                && repositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("The email is already registered");
        }

        if (!existingUser.getUsername().equals(username)
                && repositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("The username is already registered");
        }
    }


    @Override
    public User activateUser(Long id) {
        return updateStatus(id, StatusConstants.ACTIVE);
    }

    @Override
    public User deactivateUser(Long id) {
        return updateStatus(id, StatusConstants.INACTIVE);
    }

    @Override
    public User blockedUser(Long id) {
        return updateStatus(id, StatusConstants.BLOCKED);
    }

    private User updateStatus(Long id, String status) {
        User user = findById(id);
        user.setStatus(status);
        return repositoryPort.save(user);
    }

    @Override
    public User changePassword(Long userId, PasswordRequest request) {
        User user = findById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("La nueva contraseña no puede ser igual a la contraseña actual.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException("La confirmación de la contraseña no coincide.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return repositoryPort.save(user);
    }
}