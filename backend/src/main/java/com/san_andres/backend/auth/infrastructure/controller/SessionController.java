package com.san_andres.backend.auth.infrastructure.controller;

import com.san_andres.backend.auth.domain.port.usecase.SessionUseCase;
import com.san_andres.backend.auth.infrastructure.persistence.projection.ActiveSessionProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
@Tag(name = "Session")
public class SessionController {

    private final SessionUseCase sessionUseCase;

    @GetMapping
    public ResponseEntity<Page<ActiveSessionProjection>> findActiveSessions(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sessionUseCase.findActiveSessions(search, pageable));
    }

}
