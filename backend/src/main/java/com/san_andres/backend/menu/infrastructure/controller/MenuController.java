package com.san_andres.backend.menu.infrastructure.controller;

import com.san_andres.backend.menu.domain.model.Menu;
import com.san_andres.backend.menu.domain.port.usecase.MenuUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
@Tag(name = "Menu")
public class MenuController {

    private final MenuUseCase menuUseCase;

    @Operation(summary = "Get all menu")
    @GetMapping
    public ResponseEntity<List<Menu>> getAll() {
        return ResponseEntity.ok(menuUseCase.findAll());
    }

}