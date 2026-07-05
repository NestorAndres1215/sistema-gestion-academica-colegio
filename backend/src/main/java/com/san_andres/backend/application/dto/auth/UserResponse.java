package com.san_andres.backend.application.dto.auth;

import com.san_andres.backend.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserStatus status;
    private String role;
}
