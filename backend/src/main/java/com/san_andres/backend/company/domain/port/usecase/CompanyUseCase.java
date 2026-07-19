package com.san_andres.backend.company.domain.port.usecase;

import com.san_andres.backend.company.application.dto.request.CompanyRequest;
import com.san_andres.backend.company.domain.model.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CompanyUseCase {

    List<Company> findAll();

    Company findById(String id);

    Company save(MultipartFile logo, CompanyRequest companyRequest) throws IOException;

    Company update (Long id,MultipartFile logo,CompanyRequest companyRequest) throws IOException;

}