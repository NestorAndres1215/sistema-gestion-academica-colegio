package com.san_andres.backend.application.dto.excel;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    private String fileName;

    private List<String> headers;

}