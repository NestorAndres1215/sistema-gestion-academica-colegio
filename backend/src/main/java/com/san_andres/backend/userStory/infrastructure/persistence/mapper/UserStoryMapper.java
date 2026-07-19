package com.san_andres.backend.userStory.infrastructure.persistence.mapper;

import com.san_andres.backend.users.infrastructure.persistence.mapper.UserMapper;
import com.san_andres.backend.userStory.application.dto.response.UserStoryResponse;
import com.san_andres.backend.userStory.domain.model.UserStory;
import com.san_andres.backend.userStory.infrastructure.persistence.entity.UserStoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserStoryMapper {

    private final UserMapper userMapper;

    public UserStory toDomain(UserStoryEntity entity) {
        if (entity == null) return null;

        return UserStory.builder()
                .id(entity.getId())
                .action(entity.getAction())
                .module(entity.getModule())
                .detail(entity.getDetail())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .user(userMapper.toDomain(entity.getUser()))
                .build();
    }

    public UserStoryEntity toEntity(UserStory domain) {
        if (domain == null) return null;

        return UserStoryEntity.builder()
                .id(domain.getId())
                .action(domain.getAction())
                .module(domain.getModule())
                .detail(domain.getDetail())
                .createdAt(domain.getCreatedAt())
                .status(domain.getStatus())
                .user(userMapper.toEntity(domain.getUser()))
                .build();
    }

    public UserStoryResponse toResponse(UserStoryEntity entity) {

        return UserStoryResponse.builder()
                .id(entity.getId())
                .action(entity.getAction())
                .detail(entity.getDetail())
                .module(entity.getModule())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .username(entity.getUser() != null
                        ? entity.getUser().getUsername()
                        : null)
                .email(entity.getUser() != null
                        ? entity.getUser().getEmail()
                        : null)
                .build();
    }


}