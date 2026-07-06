package com.san_andres.backend.domain.models;

import com.san_andres.backend.domain.enums.UserStatus;
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