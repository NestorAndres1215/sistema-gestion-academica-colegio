package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.UserStory;
import com.san_andres.backend.infrastructure.persistence.entities.UserStoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserStoryMapper {

    UserStory toDomain(UserStoryEntity entity);

    UserStoryEntity toEntity(UserStory userStory);
}
