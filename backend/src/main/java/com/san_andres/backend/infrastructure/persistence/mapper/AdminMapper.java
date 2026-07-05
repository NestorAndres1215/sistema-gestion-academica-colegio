package com.san_andres.backend.infrastructure.persistence.mapper;


import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.infrastructure.persistence.entities.AdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AdminMapper {

    @Mapping(source = "userEntity", target = "user")
    Admin toDomain(AdminEntity entity);

    @Mapping(source = "user", target = "userEntity")
    AdminEntity toEntity(Admin admin);
}
