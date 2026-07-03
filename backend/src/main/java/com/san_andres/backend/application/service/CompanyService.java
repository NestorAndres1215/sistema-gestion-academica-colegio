package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.company.CompanyRequest;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Company;
import com.san_andres.backend.domain.port.repositories.CompanyRepositoryPort;
import com.san_andres.backend.domain.port.usecases.CompanyUseCase;
import com.san_andres.backend.domain.port.usecases.FileUseCase;
import com.san_andres.backend.infrastructure.utils.SequenceGenerator;
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
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compania No Encontrada"));
    }



    @Override
    public Company save(MultipartFile logo, CompanyRequest companyRequest) throws IOException {

        if (logo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío.");
        }
        String fileName = fileUseCase.storeFile(logo,"company");

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/assets/")
                .path(fileName)
                .toUriString();

        String newCode = SequenceGenerator.generateCode(repositoryPort.findLastCode());

        Company company = Company.builder()
                .id(newCode)
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
    public Company update(String id,MultipartFile logo, CompanyRequest companyRequest) throws IOException {

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