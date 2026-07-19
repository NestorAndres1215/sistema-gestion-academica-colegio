package com.san_andres.backend.company.domain.port.output;

import com.san_andres.backend.company.domain.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepositoryPort {

    List<Company> findAll();

    Optional<Company> findByName(String name);

    Optional<Company> findById(Long id);

    Optional<Company> findByCode(String code);

    Company save(Company company);

}