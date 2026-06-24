package com.san_andres.backend.infrastructure.persistence.repositories;


import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByStatus(UserStatus userStatus);

    List<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    boolean existsByEmail(String email);

    @Query("SELECT MAX(c.id) FROM UserEntity c")
    String findLastCode();
}
