package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JpaMenuRepository extends JpaRepository<MenuEntity,String> {

    @Query("SELECT m FROM MenuEntity m LEFT JOIN FETCH m.children")
    List<MenuEntity> findAllWithChildren();

}