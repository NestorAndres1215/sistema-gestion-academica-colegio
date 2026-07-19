package com.san_andres.backend.userStory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserStoryResponse {
    private Long id;
    private String action;
    private String detail;
    private String module;
    private LocalDateTime createdAt;
    private String status;
    private String username;
    private String email;

}
