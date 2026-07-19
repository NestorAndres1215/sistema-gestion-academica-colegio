package com.san_andres.backend.auth.infrastructure.adapter.output.persistence.mapper;

import com.san_andres.backend.auth.infrastructure.adapter.output.persistence.entity.SessionEntity;
import com.san_andres.backend.auth.domain.model.Session;
import com.san_andres.backend.users.infrastructure.adapter.output.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    private final UserMapper userMapper;

    public SessionMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Session toDomain(SessionEntity entity) {
        if (entity == null) return null;

        return Session.builder()
                .id(entity.getId())
                .loginAt(entity.getLoginAt())
                .logoutAt(entity.getLogoutAt())
                .isActive(entity.getIsActive())
                .ipAddress(entity.getIpAddress())
                .location(entity.getLocation())
                .userAgent(entity.getUserAgent())
                .user(
                        entity.getUser() == null ? null :
                                userMapper.toDomain(entity.getUser())
                )
                .build();
    }

    public SessionEntity toEntity(Session domain) {
        if (domain == null) return null;

        return SessionEntity.builder()
                .id(domain.getId())
                .loginAt(domain.getLoginAt())
                .logoutAt(domain.getLogoutAt())
                .isActive(domain.getIsActive())
                .ipAddress(domain.getIpAddress())
                .location(domain.getLocation())
                .userAgent(domain.getUserAgent())
                .user(
                        domain.getUser() == null ? null :
                                userMapper.toEntity(domain.getUser())
                )
                .build();
    }
}