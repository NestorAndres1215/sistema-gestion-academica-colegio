package com.san_andres.backend.infrastructure.security;

import com.san_andres.backend.application.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException {

        ErrorResponse error = ErrorResponse.builder()
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Authentication required.")
                .timestamp(Instant.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .traceId(UUID.randomUUID().toString())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        objectMapper.writeValue(response.getWriter(), error);
    }
}