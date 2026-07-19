package com.san_andres.backend.userStory.domain.model;

import com.san_andres.backend.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {

    private Long id;
    private String action;
    private String detail;
    private String module;
    private LocalDateTime createdAt;
    private String status;
    private User user;

}