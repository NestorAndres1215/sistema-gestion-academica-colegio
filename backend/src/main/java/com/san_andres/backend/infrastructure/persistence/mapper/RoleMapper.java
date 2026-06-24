package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Role;
import com.san_andres.backend.infrastructure.persistence.entities.RoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role role);

    List<Role> toDomainList(List<RoleEntity> entities);

}