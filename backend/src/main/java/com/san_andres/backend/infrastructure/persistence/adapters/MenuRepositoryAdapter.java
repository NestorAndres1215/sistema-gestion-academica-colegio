package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.models.Menu;
import com.san_andres.backend.domain.port.repositories.MenuRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.mapper.MenuMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaMenuRepository;
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
        Set<String> visited = new HashSet<>();
        return repository.findAllWithChildren().stream()
                .map(menuEntity -> mapper.toDomain(menuEntity, visited))
                .toList();
    }
}