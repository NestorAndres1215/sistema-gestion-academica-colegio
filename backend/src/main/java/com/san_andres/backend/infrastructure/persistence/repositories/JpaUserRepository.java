package com.san_andres.backend.infrastructure.persistence.repositories;


import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmailOrUsername(String email, String username);

    List<UserEntity> findByStatus(UserStatus userStatus);

    List<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
