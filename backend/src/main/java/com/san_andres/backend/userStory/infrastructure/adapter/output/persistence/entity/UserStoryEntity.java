package com.san_andres.backend.userStory.infrastructure.adapter.output.persistence.entity;

import com.san_andres.backend.users.infrastructure.adapter.output.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_story")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String action;
    private String module;
    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}