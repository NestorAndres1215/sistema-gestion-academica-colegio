package com.san_andres.backend.domain.models;


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
    private String isActive;
    private String ipAddress;
    private String location;
    private String userAgent;
    private User user;

}
