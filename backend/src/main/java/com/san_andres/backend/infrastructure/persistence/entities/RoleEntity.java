package com.san_andres.backend.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(length = 8)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<MenuEntity> menus;

}
