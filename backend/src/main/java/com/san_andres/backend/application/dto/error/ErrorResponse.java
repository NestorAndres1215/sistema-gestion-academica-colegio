package com.san_andres.backend.application.dto.error;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private String error;
    private Object message;

    private String timestamp;

    private int status;
    private String traceId;
}