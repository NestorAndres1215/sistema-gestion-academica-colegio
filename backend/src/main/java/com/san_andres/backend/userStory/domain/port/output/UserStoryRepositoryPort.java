package com.san_andres.backend.userStory.domain.port.output;

import com.san_andres.backend.userStory.application.dto.response.UserStoryResponse;
import com.san_andres.backend.userStory.domain.model.UserStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserStoryRepositoryPort {

    Optional<UserStory> findById(Long id);

    UserStory save (UserStory userHistory);

    Page<UserStoryResponse> findWithFilters(
            String email,
            String status,
            String action,
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            Pageable pageable
    );

}