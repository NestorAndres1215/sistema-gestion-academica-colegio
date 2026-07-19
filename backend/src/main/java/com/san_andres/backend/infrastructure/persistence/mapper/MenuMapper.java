package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Menu;
import com.san_andres.backend.infrastructure.persistence.entities.MenuEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuMapper {

        private final RoleMapper roleMapper;

        public Menu toDomain(MenuEntity menuEntity, Set<Long> visitedIds) {

                if (menuEntity == null || visitedIds.contains(menuEntity.getId()))
                        return null;

                visitedIds.add(menuEntity.getId());

                return Menu.builder()
                                .id(menuEntity.getId())
                                .code(menuEntity.getCode())
                                .name(menuEntity.getName())
                                .icon(menuEntity.getIcon())
                                .route(menuEntity.getRoute())
                                .menuOrder(menuEntity.getMenuOrder())
                                .category(menuEntity.getCategory())

                                .parent(toDomain(menuEntity.getParent(), visitedIds))

                                .children(menuEntity.getChildren() != null
                                                ? menuEntity.getChildren().stream()
                                                                .map(child -> toDomain(child, visitedIds))
                                                                .collect(Collectors.toSet())
                                                : Set.of())

                                .roles(menuEntity.getRoles() != null
                                                ? menuEntity.getRoles().stream()
                                                                .map(roleMapper::toDomain)
                                                                .collect(Collectors.toSet())
                                                : Set.of())

                                .build();
        }

        public MenuEntity toEntity(Menu menu) {
                if (menu == null)
                        return null;

                return MenuEntity.builder()
                                .id(menu.getId())
                                .code(menu.getCode())
                                .name(menu.getName())
                                .icon(menu.getIcon())
                                .route(menu.getRoute())
                                .menuOrder(menu.getMenuOrder())
                                .category(menu.getCategory())

                                .parent(toEntity(menu.getParent()))

                                .children(menu.getChildren() != null
                                                ? menu.getChildren().stream()
                                                                .map(this::toEntity)
                                                                .collect(Collectors.toSet())
                                                : Set.of())

                                .roles(menu.getRoles() != null
                                                ? menu.getRoles().stream()
                                                                .map(roleMapper::toEntity)
                                                                .collect(Collectors.toSet())
                                                : Set.of())

                                .build();
        }
}