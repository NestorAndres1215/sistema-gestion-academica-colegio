package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.domain.port.repositories.RoleRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.mapper.RoleMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final JpaRoleRepository repository;
    private  final RoleMapper mapper;

    @Override
    public Page<Role> getAll(String search, Pageable pageable) {
        return repository.search(search, pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}