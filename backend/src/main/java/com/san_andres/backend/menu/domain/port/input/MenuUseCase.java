package com.san_andres.backend.menu.domain.port.input;

import com.san_andres.backend.menu.domain.model.Menu;

import java.util.List;

public interface MenuUseCase {
    List<Menu> findAll();
}
