package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.admin.AdminRequest;
import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.application.dto.report.ImportResult;
import com.san_andres.backend.domain.exceptions.DuplicateResourceException;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.AdminRepositoryPort;
import com.san_andres.backend.domain.port.usecases.AdminUseCase;
import com.san_andres.backend.domain.port.usecases.FileUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import com.san_andres.backend.infrastructure.persistence.mapper.AdminExcelMapper;
import com.san_andres.backend.infrastructure.reports.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminUseCase {

    private final AdminRepositoryPort repositoryPort;
    private final UserUseCase userUseCase;
    private final FileUseCase fileUseCase;
    private final ExcelReader excelReader;

    private final AdminExcelMapper excelMapper;

    @Override
    public Admin findById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));
    }

    @Override
    public List<Admin> findAll() {
        return repositoryPort.findAll();
    }


    @Override
    public Admin save(AdminRequest administratorRequest) {

        if (repositoryPort.existsByDni(administratorRequest.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado.");
        }

        if (administratorRequest.getPhone() != null && repositoryPort.existsByPhone(administratorRequest.getPhone())) {
            throw new DuplicateResourceException("El número de teléfono ya está registrado.");
        }

        User user = userUseCase.save( administratorRequest.getEmail(), administratorRequest.getUsername(), administratorRequest.getPassword(), "ROLE_ADMINISTRATOR");

        Admin administrator = Admin.builder()
                .firstName(administratorRequest.getFirstName())
                .middleName(administratorRequest.getMiddleName())
                .paternalLastName(administratorRequest.getPaternalLastName())
                .maternalLastName(administratorRequest.getMaternalLastName())
                .dni(administratorRequest.getDni())
                .phone(administratorRequest.getPhone())
                .birthDate(administratorRequest.getBirthDate())
                .profile("")
                .gender(administratorRequest.getGender())
                .nationality(administratorRequest.getNationality())
                .status("ACTIVE")
                .user(user)
                .build();

        return repositoryPort.save(administrator);
    }

    @Override
    public Admin update(Long id, MultipartFile file, AdminRequest administratorRequest) {

        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado"));

        if (!existing.getDni().equals(administratorRequest.getDni()) &&
                repositoryPort.existsByDni(administratorRequest.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado.");
        }

        if (administratorRequest.getPhone() != null &&
                !administratorRequest.getPhone().equals(existing.getPhone()) &&
                repositoryPort.existsByPhone(administratorRequest.getPhone())) {
            throw new DuplicateResourceException("El número de teléfono ya está registrado.");
        }

        existing.setFirstName(administratorRequest.getFirstName());
        existing.setMiddleName(administratorRequest.getMiddleName());
        existing.setPaternalLastName(administratorRequest.getPaternalLastName());
        existing.setMaternalLastName(administratorRequest.getMaternalLastName());
        existing.setDni(administratorRequest.getDni());
        existing.setPhone(administratorRequest.getPhone());
        existing.setBirthDate(administratorRequest.getBirthDate());
        existing.setGender(administratorRequest.getGender());
        existing.setNationality(administratorRequest.getNationality());

        // Actualizar la imagen solo si se envió un archivo
        if (file != null && !file.isEmpty()) {

            String fileName = fileUseCase.storeFile(file, "admin");

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/assets/")
                    .path(fileName)
                    .toUriString();

            existing.setProfile(fileUrl);
        }

        return repositoryPort.save(existing);
    }

    @Override
    public Page<AdminResponse> getByStatus(String status, String search, Pageable pageable) {
        return repositoryPort.getByStatus(status, search, pageable);
    }

    @Override
    public Admin deactivate(Long id) {
        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator no encontrado"));
        userUseCase.deactivateUser(existing.getUser().getId());
        existing.setStatus("INACTIVE");
        return repositoryPort.save(existing);
    }

    @Override
    public Admin activate(Long id) {
        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator no encontrado"));
        userUseCase.activateUser(existing.getUser().getId());
        existing.setStatus("ACTIVE");
        return repositoryPort.save(existing);
    }

    @Override
    public Optional<AdminResponse> findByEmail(String email) {
        return repositoryPort.findByEmail(email);
    }

    @Override
    public List<AdminResponse> search(String search) {

        if (search == null || search.isBlank()) {
            return repositoryPort.findRandom(5);
        }

        return repositoryPort.search(search.trim(), 5);
    }

    @Override
    public List<Admin> saveAll(List<AdminRequest> requests){

        List<Admin> result = new ArrayList<>();

        for(AdminRequest request: requests){
            result.add(save(request));
        }

        return result;
    }

    @Override
    public ImportResult importExcel(MultipartFile file) {

         try {

             List<List<String>> rows = excelReader.read(file);

             List<AdminRequest> requests = rows.stream()
                     .map(excelMapper::toRequest)
                     .toList();

             List<Admin> admins = saveAll(requests);

             return new ImportResult(
                     requests.size(),
                     admins.size(),
                     0,
                     List.of()
             );

         } catch (Exception e) {
             throw new RuntimeException(e);
         }

    }
}
