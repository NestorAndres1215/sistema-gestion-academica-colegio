package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Token;
import com.san_andres.backend.infrastructure.persistence.entities.TokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SessionMapper.class})
public interface TokenMapper {

    Token toDomain(TokenEntity entity);

    TokenEntity toEntity(Token user);
}
