package com.san_andres.backend.userStory.domain.port.input;

import com.san_andres.backend.userStory.application.dto.request.UserStoryRequest;

import com.san_andres.backend.userStory.application.dto.response.UserStoryResponse;
import com.san_andres.backend.userStory.domain.model.UserStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserStoryUseCase {

    UserStory save (UserStoryRequest userStoryRequest);

    Page<UserStoryResponse> findWithFilters(String email, String status, String action,
                                            LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    UserStory activate (Long id);

    UserStory deactivate (Long id);
}