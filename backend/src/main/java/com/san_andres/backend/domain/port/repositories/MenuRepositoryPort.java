package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Menu;

import java.util.List;

public interface MenuRepositoryPort {
    List<Menu>findAll();
}
