package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .roles(
                        entity.getRoles() == null ? null :
                                entity.getRoles()
                                        .stream()
                                        .map(roleMapper::toDomain)
                                        .toList()
                )
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;

        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .roles(
                        domain.getRoles() == null ? null :
                                domain.getRoles()
                                        .stream()
                                        .map(roleMapper::toEntity)
                                        .toList()
                )
                .build();
    }
}