package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Session;
import com.san_andres.backend.infrastructure.persistence.entities.SessionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SessionMapper {

    Session toDomain(SessionEntity entity);
    SessionEntity toEntity(Session user);
}
