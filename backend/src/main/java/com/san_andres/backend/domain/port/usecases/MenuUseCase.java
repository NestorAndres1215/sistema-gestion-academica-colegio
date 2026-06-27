package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.domain.models.Menu;

import java.util.List;

public interface MenuUseCase {
    List<Menu> findAll();
}
