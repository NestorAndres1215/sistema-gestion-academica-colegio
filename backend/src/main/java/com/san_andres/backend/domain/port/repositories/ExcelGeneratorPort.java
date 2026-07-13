package com.san_andres.backend.domain.port.repositories;

import java.util.List;

public interface ExcelGeneratorPort {

    byte[] generateList(String title, List<String> headers, List<List<String>> rows);

}