package com.san_andres.backend.report.application.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    private String fileName;

    private List<String> headers;

}