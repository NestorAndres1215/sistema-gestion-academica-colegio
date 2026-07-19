package com.san_andres.backend.userStory.application.service;

import com.san_andres.backend.userStory.application.dto.request.UserStoryRequest;
import com.san_andres.backend.userStory.application.dto.response.UserStoryResponse;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.userStory.domain.model.UserStory;
import com.san_andres.backend.userStory.domain.port.output.UserStoryRepositoryPort;
import com.san_andres.backend.userStory.domain.port.input.UserStoryUseCase;
import com.san_andres.backend.users.domain.port.input.UserUseCase;
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

        User user = userUseCase.findByEmail(userStoryRequest.getEmail());
        UserStory userStory = UserStory.builder()
                .action(userStoryRequest.getAction())
                .detail(userStoryRequest.getDetail())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        return repositoryPort.save(userStory);
    }

    @Override
    public Page<UserStoryResponse> findWithFilters(String email, String status, String action,
            LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable) {
        return repositoryPort.findWithFilters(email, status, action, dateFrom, dateTo, pageable);
    }

    @Override
    public UserStory activate(Long id) {

        UserStory existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado"));

        existing.setStatus("ACTIVE");
        return repositoryPort.save(existing);
    }

    @Override
    public UserStory deactivate(Long id) {
        UserStory existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado"));

        existing.setStatus("INACTIVE");
        return repositoryPort.save(existing);
    }
}