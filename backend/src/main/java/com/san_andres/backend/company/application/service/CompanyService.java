package com.san_andres.backend.company.application.service;

import com.san_andres.backend.company.application.dto.request.CompanyRequest;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.company.domain.model.Company;
import com.san_andres.backend.company.domain.port.output.CompanyRepositoryPort;
import com.san_andres.backend.company.domain.port.input.CompanyUseCase;
import com.san_andres.backend.file.domain.port.input.FileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService implements CompanyUseCase {

    private final CompanyRepositoryPort repositoryPort;
    private final FileUseCase fileUseCase;

    @Override
    public List<Company> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public Company findById(String id) {
        return repositoryPort.findByCode(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compania No Encontrada"));
    }

    @Override
    public Company save(MultipartFile logo, CompanyRequest companyRequest) throws IOException {

        if (logo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío.");
        }

        String fileName = fileUseCase.storeFile(logo, "company");

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/assets/")
                .path(fileName)
                .toUriString();

        Company company = Company.builder()
                .name(companyRequest.getName())
                .logo(fileUrl)
                .ruc(companyRequest.getRuc())
                .description(companyRequest.getDescription())
                .phone(companyRequest.getPhone())
                .email(companyRequest.getEmail())
                .address(companyRequest.getAddress())
                .city(companyRequest.getCity())
                .country(companyRequest.getCountry())
                .companyType(companyRequest.getCompanyType())
                .foundingDate(companyRequest.getFoundingDate())
                .createdAt(LocalDateTime.now())
                .build();

        return repositoryPort.save(company);
    }

    @Override
    public Company update(Long id, MultipartFile logo, CompanyRequest companyRequest) throws IOException {

        Company existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compania  No Encontrado"));

        if (logo != null && !logo.isEmpty()) {

            if (existing.getLogo() != null && !existing.getLogo().isBlank()) {
                System.out.println(existing.getLogo());
                fileUseCase.deleteFile(existing.getLogo());
            }

            String fileName = fileUseCase.storeFile(logo, "company");

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/assets/")
                    .path(fileName)
                    .toUriString();

            existing.setLogo(fileUrl);
        }
        existing.setName(companyRequest.getName());
        existing.setRuc(companyRequest.getRuc());
        existing.setDescription(companyRequest.getDescription());
        existing.setPhone(companyRequest.getPhone());
        existing.setEmail(companyRequest.getEmail());
        existing.setAddress(companyRequest.getAddress());
        existing.setCity(companyRequest.getCity());
        existing.setCountry(companyRequest.getCountry());
        existing.setCompanyType(companyRequest.getCompanyType());
        existing.setFoundingDate(companyRequest.getFoundingDate());
        existing.setUpdatedAt(LocalDateTime.now());

        return repositoryPort.save(existing);
    }

}