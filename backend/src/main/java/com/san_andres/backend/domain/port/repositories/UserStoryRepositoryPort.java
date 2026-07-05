package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.UserStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserStoryRepositoryPort {

    Optional<UserStory> findById(Long id);

    UserStory save (UserStory userHistory);

    Page<UserStory> findWithFilters(String email, UserStatus status, String action, Pageable pageable);


}