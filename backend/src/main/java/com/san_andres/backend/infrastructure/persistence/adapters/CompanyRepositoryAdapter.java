package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.models.Company;
import com.san_andres.backend.domain.port.repositories.CompanyRepositoryPort;
import com.san_andres.backend.infrastructure.persistence.entities.CompanyEntity;
import com.san_andres.backend.infrastructure.persistence.mapper.CompanyMapper;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final JpaCompanyRepository repository;
    private final CompanyMapper mapper;


    @Override
    public List<Company> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Company> findByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }
    @Override
    public Optional<Company> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = mapper.toEntity(company);
        CompanyEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

}