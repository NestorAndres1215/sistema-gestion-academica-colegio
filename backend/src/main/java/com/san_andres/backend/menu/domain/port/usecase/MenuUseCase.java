package com.san_andres.backend.menu.domain.port.usecase;

import com.san_andres.backend.menu.domain.model.Menu;

import java.util.List;

public interface MenuUseCase {
    List<Menu> findAll();
}
