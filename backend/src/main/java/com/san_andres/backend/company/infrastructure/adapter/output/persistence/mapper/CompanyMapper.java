package com.san_andres.backend.company.infrastructure.adapter.output.persistence.mapper;

import com.san_andres.backend.company.domain.model.Company;
import com.san_andres.backend.company.infrastructure.adapter.output.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toDomain(CompanyEntity entity) {
        if (entity == null)
            return null;

        return Company.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .logo(entity.getLogo())
                .ruc(entity.getRuc())
                .description(entity.getDescription())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .city(entity.getCity())
                .country(entity.getCountry())
                .companyType(entity.getCompanyType())
                .foundingDate(entity.getFoundingDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public CompanyEntity toEntity(Company domain) {
        if (domain == null)
            return null;

        return CompanyEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .name(domain.getName())
                .logo(domain.getLogo())
                .ruc(domain.getRuc())
                .description(domain.getDescription())
                .phone(domain.getPhone())
                .email(domain.getEmail())
                .address(domain.getAddress())
                .city(domain.getCity())
                .country(domain.getCountry())
                .companyType(domain.getCompanyType())
                .foundingDate(domain.getFoundingDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}