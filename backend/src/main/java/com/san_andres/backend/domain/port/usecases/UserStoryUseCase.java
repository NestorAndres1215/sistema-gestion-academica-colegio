package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.userStory.UserStoryRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.UserStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserStoryUseCase {

    UserStory save (UserStoryRequest userStoryRequest);

    Page<UserStory> findWithFilters(String email, UserStatus status, String action, Pageable pageable);

    String findLastCode();

    UserStory activate (String id);

    UserStory deactivate (String id);
}