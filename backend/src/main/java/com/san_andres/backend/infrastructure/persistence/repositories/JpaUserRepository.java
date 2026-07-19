package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmailOrUsername(String email, String username);

    List<UserEntity> findByStatus(String userStatus);

    List<UserEntity> findByEmailAndStatus(String email, String userStatus);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
