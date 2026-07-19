package com.san_andres.backend.menu.domain.port.output;

import com.san_andres.backend.menu.domain.model.Menu;

import java.util.List;

public interface MenuRepositoryPort {
    List<Menu>findAll();
}
