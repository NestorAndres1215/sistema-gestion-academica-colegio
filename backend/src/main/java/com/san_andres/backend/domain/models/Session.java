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
public class Session {

    private Long id;
    private LocalDateTime loginAt;
    private LocalDateTime logoutAt;
    private UserStatus isActive;
    private String ipAddress;
    private String location;
    private String userAgent;
    private User user;

}
