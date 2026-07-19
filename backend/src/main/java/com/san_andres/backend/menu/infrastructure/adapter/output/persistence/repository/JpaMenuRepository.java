package com.san_andres.backend.menu.infrastructure.adapter.output.persistence.repository;

import com.san_andres.backend.menu.infrastructure.adapter.output.persistence.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JpaMenuRepository extends JpaRepository<MenuEntity,Long> {

    @Query("""
        SELECT DISTINCT m
        FROM MenuEntity m
        LEFT JOIN FETCH m.children
        LEFT JOIN FETCH m.roles
        LEFT JOIN FETCH m.parent
        """)
    List<MenuEntity> findAllWithChildren();

}