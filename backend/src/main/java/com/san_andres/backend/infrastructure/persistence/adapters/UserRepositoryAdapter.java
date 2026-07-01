package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.UserRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;
import com.san_andres.backend.infrastructure.persistence.mapper.UserMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository repository;
    private final UserMapper mapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository
                .findByEmailOrUsername(login, login)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findByStatus(UserStatus userStatus) {
        return repository.findByStatus(userStatus).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return repository.findByEmailAndStatus(email, userStatus).stream().map(mapper::toDomain).toList();
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public String findLastCode() {
        return repository.findLastCode();
    }
}