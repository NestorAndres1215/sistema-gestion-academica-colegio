package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.domain.models.Company;
import com.san_andres.backend.infrastructure.persistence.entities.CompanyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toDomain(CompanyEntity entity);
    CompanyEntity toEntity(Company company);
}