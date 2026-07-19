package com.san_andres.backend.menu.infrastructure.persistence.adapter;

import com.san_andres.backend.menu.domain.model.Menu;
import com.san_andres.backend.menu.domain.port.repository.MenuRepositoryPort;
import com.san_andres.backend.menu.infrastructure.persistence.mapper.MenuMapper;
import com.san_andres.backend.menu.infrastructure.persistence.repository.JpaMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final JpaMenuRepository repository;
    private final MenuMapper mapper;

    @Override
    public List<Menu> findAll() {
        Set<Long> visited = new HashSet<>();
        return repository.findAllWithChildren().stream()
                .map(menuEntity -> mapper.toDomain(menuEntity, visited))
                .toList();
    }
}