package com.san_andres.backend.users.infrastructure.persistence.adapter;


import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.users.domain.port.repository.UserRepositoryPort;
import com.san_andres.backend.users.infrastructure.persistence.entity.UserEntity;
import com.san_andres.backend.users.infrastructure.persistence.mapper.UserMapper;
import com.san_andres.backend.users.infrastructure.persistence.repository.JpaUserRepository;
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
    public List<User> findByStatus(String status) {
        return repository.findByStatus(status).stream().map(mapper::toDomain).toList();
    }

    @Override
        public List<User> findByEmailAndStatus(String email, String status) {
        return repository.findByEmailAndStatus(email, status).stream().map(mapper::toDomain).toList();
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
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }


}