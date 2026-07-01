package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.userStory.UserStoryRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.models.UserStory;
import com.san_andres.backend.domain.port.repositories.UserStoryRepositoryPort;
import com.san_andres.backend.domain.port.usecases.UserStoryUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import com.san_andres.backend.infrastructure.utils.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserStoryService implements UserStoryUseCase {

    private final UserStoryRepositoryPort repositoryPort;
    private final UserUseCase userUseCase;

    @Override
    public UserStory save(UserStoryRequest userStoryRequest) {
        String newCode = SequenceGenerator.generateCode(repositoryPort.findLastCode());
        User user = userUseCase.findByEmail(userStoryRequest.getEmail());
        UserStory userStory= UserStory.builder()
                .id(newCode)
                .action(userStoryRequest.getAction())
                .detail(userStoryRequest.getDetail())
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        return repositoryPort.save(userStory);
    }

    @Override
    public Page<UserStory> findWithFilters(String email, UserStatus status, String action, Pageable pageable) {
        return repositoryPort.findWithFilters(email, status, action, pageable);
    }

    @Override
    public String findLastCode() {
        return repositoryPort.findLastCode();
    }

    @Override
    public UserStory activate(String id) {

        UserStory existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));

        existing.setStatus(UserStatus.ACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public UserStory deactivate(String id) {
        UserStory existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));

        existing.setStatus(UserStatus.INACTIVE);
        return repositoryPort.save(existing);
    }
}