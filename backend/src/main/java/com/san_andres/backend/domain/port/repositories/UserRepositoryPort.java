package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);

    List<User> findByEmailAndStatus(String email, UserStatus status);

    User save (User user);

    boolean existsByEmail(String email);

    Optional<User> findById(String id);

    String findLastCode();
}