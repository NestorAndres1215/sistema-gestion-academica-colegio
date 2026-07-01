package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.domain.models.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepositoryPort {

    List<Company> findAll();

    Optional<Company> findByName(String name);

    Optional<Company> findById(String id);

    Company save(Company company);

    String findLastCode();
}