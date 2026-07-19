package com.san_andres.backend.menu.application.service;

import com.san_andres.backend.menu.domain.model.Menu;
import com.san_andres.backend.menu.domain.port.repository.MenuRepositoryPort;
import com.san_andres.backend.menu.domain.port.usecase.MenuUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    @Override
    public List<Menu> findAll() {
        return menuRepositoryPort.findAll();
    }

}
