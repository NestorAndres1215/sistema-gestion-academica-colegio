package com.san_andres.backend.domain.port.repositories;

import java.util.List;

public interface PdfGeneratorPort {

    byte[] generateList(String title, List<String> headers, List<List<String>> rows);
    byte[] generateListId(String title, List<String> headers, List<List<String>> rows);
}