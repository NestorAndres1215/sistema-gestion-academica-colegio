package com.san_andres.backend.admin.application.service;

import com.san_andres.backend.admin.application.dto.request.AdminRequest;
import com.san_andres.backend.admin.application.dto.response.AdminResponse;
import com.san_andres.backend.report.application.dto.reponse.ImportResult;
import com.san_andres.backend.shared.constants.RoleConstants;
import com.san_andres.backend.shared.constants.StatusConstants;
import com.san_andres.backend.shared.exception.DuplicateResourceException;
import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.admin.domain.model.Admin;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.admin.domain.port.repository.AdminRepositoryPort;
import com.san_andres.backend.admin.domain.port.usecase.AdminUseCase;
import com.san_andres.backend.file.domain.port.usecase.FileUseCase;
import com.san_andres.backend.users.domain.port.usecase.UserUseCase;
import com.san_andres.backend.report.infrastructure.persistence.mapper.AdminExcelMapper;
import com.san_andres.backend.shared.excel.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

        User user = userUseCase.save(administratorRequest.getEmail(), administratorRequest.getUsername(),
                administratorRequest.getPassword(), RoleConstants.ROLE_ADMIN);

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

        Admin existing = findById(id);

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
        Admin existing = findById(id);
        userUseCase.deactivateUser(existing.getUser().getId());
        existing.setStatus(StatusConstants.INACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public Admin activate(Long id) {
        Admin existing = findById(id);
        userUseCase.activateUser(existing.getUser().getId());
        existing.setStatus(StatusConstants.ACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public Admin blocked(Long id) {
        Admin existing = findById(id);
        userUseCase.blockedUser(existing.getUser().getId());
        existing.setStatus(StatusConstants.BLOCKED);
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
    public List<Admin> saveAll(List<AdminRequest> requests) {

        List<Admin> result = new ArrayList<>();

        for (AdminRequest request : requests) {
            result.add(save(request));
        }

        return result;
    }

    @Override
    public ImportResult importExcel(MultipartFile file) throws Exception {
        List<List<String>> rows = excelReader.read(file);

        List<AdminRequest> requests = rows.stream()
                .map(excelMapper::toRequest)
                .toList();

        List<Admin> admins = saveAll(requests);

        return new ImportResult(
                requests.size(),
                admins.size(),
                0,
                List.of());


    }
}
