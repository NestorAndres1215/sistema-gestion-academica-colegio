package com.san_andres.backend.menu.infrastructure.adapter.output.persistence.entity;

import com.san_andres.backend.role.infrastructure.adapter.output.persistence.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String icon;

    private String route;

    @Column(name = "menu_order")
    private Integer menuOrder;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MenuEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<MenuEntity> children;

    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
    private Set<RoleEntity> roles;
}