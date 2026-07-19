package com.san_andres.backend.role.domain.port.output;

import com.san_andres.backend.role.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleRepositoryPort {

    Page<Role> getAll(String search, Pageable pageable);

    Optional<Role> findByName(String name);

    Optional<Role> findById(Long id);

}