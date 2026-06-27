package com.san_andres.backend.application.service;

import com.san_andres.backend.domain.models.Menu;
import com.san_andres.backend.domain.port.repositories.MenuRepositoryPort;
import com.san_andres.backend.domain.port.usecases.MenuUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuUseCase {

    private  final MenuRepositoryPort menuRepositoryPort;

    @Override
    public List<Menu> findAll() {
        return menuRepositoryPort.findAll();
    }

}
