package com.san_andres.backend.application.service;

import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.port.repositories.RoleRepositoryPort;
import com.san_andres.backend.domain.port.usecases.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleUseCase {

    private final RoleRepositoryPort repositoryPort;

    @Override
    public Page<Role> getAll(String search, Pageable pageable) {
        return repositoryPort.getAll(search, pageable);
    }

    @Override
    public Role findByName(String name) {
        return repositoryPort.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }

    @Override
    public Role findById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }
}