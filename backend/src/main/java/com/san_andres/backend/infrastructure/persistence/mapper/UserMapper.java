package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.infrastructure.persistence.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toDomain(UserEntity entity);
    UserEntity toEntity(User user);
}