package com.san_andres.backend.domain.models;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Role> roles;
}
