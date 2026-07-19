package com.san_andres.backend.shared.security.handler;

import com.san_andres.backend.shared.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override

    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex) throws IOException {

        ErrorResponse error = ErrorResponse.builder()
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("Access denied.")
                .timestamp(Instant.now().toString())
                .status(HttpStatus.FORBIDDEN.value())
                .traceId(UUID.randomUUID().toString())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        objectMapper.writeValue(response.getWriter(), error);
    }
}