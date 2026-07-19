package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.application.dto.userStory.UserStoryResponse;

import com.san_andres.backend.domain.models.UserStory;
import com.san_andres.backend.domain.port.repositories.UserStoryRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.entities.UserStoryEntity;
import com.san_andres.backend.infrastructure.persistence.mapper.UserStoryMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaUserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserStoryRepositoryAdapter implements UserStoryRepositoryPort {

    private final JpaUserStoryRepository userHistoryRepository;
    private final UserStoryMapper mapper;

    @Override
    public Optional<UserStory> findById(Long id) {
        return userHistoryRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public UserStory save(UserStory userHistory) {
        UserStoryEntity entity = mapper.toEntity(userHistory);
        UserStoryEntity saved = userHistoryRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Page<UserStoryResponse> findWithFilters(
            String email,
            String status,
            String action,
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            Pageable pageable) {
        return userHistoryRepository.findWithFilters(
                email,
                status,
                action,
                dateFrom,
                dateTo,
                pageable).map(mapper::toResponse);
    }

}
