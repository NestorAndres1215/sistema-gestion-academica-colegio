package com.san_andres.backend.company.infrastructure.persistence.repository;

import com.san_andres.backend.company.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByName(String name);

    Optional<CompanyEntity> findByCode(String code);

}