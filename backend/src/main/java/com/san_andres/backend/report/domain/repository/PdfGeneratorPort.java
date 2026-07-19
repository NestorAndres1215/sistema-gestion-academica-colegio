package com.san_andres.backend.report.domain.repository;

import java.util.List;

public interface PdfGeneratorPort {

    byte[] generateList(String title, List<String> headers, List<List<String>> rows);

    byte[] generateListId(String title, List<String> headers, List<List<String>> rows);

}