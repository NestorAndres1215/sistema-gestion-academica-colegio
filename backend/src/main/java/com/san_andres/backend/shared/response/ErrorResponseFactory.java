package com.san_andres.backend.shared.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ErrorResponseFactory {

    public ErrorResponse create(String message, HttpStatus status) {

        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now().toString())
                .traceId(UUID.randomUUID().toString())
                .build();
    }

}