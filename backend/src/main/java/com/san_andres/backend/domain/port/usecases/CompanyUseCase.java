package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.company.CompanyRequest;
import com.san_andres.backend.domain.models.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CompanyUseCase {

    List<Company> findAll();

    Company findById(String id);

    Company save(MultipartFile logo, CompanyRequest companyRequest) throws IOException;

    Company update (String id,MultipartFile logo,CompanyRequest companyRequest) throws IOException;

}