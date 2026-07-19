package com.san_andres.backend.report.application.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportResult {

    private int total;
    private int success;
    private int errors;
    private List<String> messages;
}