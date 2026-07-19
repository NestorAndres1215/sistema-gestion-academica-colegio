package com.san_andres.backend.domain.port.repositories;


import com.san_andres.backend.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByLogin(String login);

    List<User> findByStatus(String status);

    List<User> findByEmailAndStatus(String email, String status);

    User save (User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}