package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.userStory.UserStoryRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.UserStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserStoryUseCase {

    UserStory save (UserStoryRequest userStoryRequest);

    Page<UserStory> findWithFilters(            String email,
                                                String status,
                                                String action,
                                                LocalDateTime dateFrom,
                                                LocalDateTime dateTo,

                                                Pageable pageable);

    UserStory activate (Long id);

    UserStory deactivate (Long id);
}