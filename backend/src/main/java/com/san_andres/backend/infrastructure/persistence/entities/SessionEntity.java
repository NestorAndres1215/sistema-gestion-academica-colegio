package com.san_andres.backend.infrastructure.persistence.entities;

import com.san_andres.backend.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false)
    private UserStatus isActive;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "location", length = 150)
    private String location;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
