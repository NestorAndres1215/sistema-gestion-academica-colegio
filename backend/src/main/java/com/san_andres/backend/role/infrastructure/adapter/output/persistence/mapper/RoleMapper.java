package com.san_andres.backend.role.infrastructure.adapter.output.persistence.mapper;

import com.san_andres.backend.role.domain.model.Role;
import com.san_andres.backend.role.infrastructure.adapter.output.persistence.entity.RoleEntity;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        if (entity == null)
            return null;

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public RoleEntity toEntity(Role domain) {
        if (domain == null)
            return null;

        return RoleEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}