package com.san_andres.backend.role.domain.port.usecase;

import com.san_andres.backend.role.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleUseCase {

    Page<Role> getAll(String search, Pageable pageable);

    Role findByName(String name);

    Role findById(Long id);

}
