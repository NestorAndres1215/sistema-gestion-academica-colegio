package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleUseCase {

    Page<Role> getAll(String search, Pageable pageable);

    Role findByName(String name);

    Role findById(String id);

}
