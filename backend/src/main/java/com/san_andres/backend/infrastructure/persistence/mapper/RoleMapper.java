package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.infrastructure.persistence.entities.RoleEntity;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        if (entity == null) return null;

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public RoleEntity toEntity(Role domain) {
        if (domain == null) return null;

        return RoleEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}