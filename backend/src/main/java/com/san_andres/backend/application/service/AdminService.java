package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.admin.AdminRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.DuplicateResourceException;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.AdminRepositoryPort;
import com.san_andres.backend.domain.port.usecases.AdminUseCase;
import com.san_andres.backend.domain.port.usecases.FileUseCase;
import com.san_andres.backend.domain.port.usecases.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminUseCase {

    private final AdminRepositoryPort repositoryPort;
    private final UserUseCase userUseCase;
    private final FileUseCase fileUseCase;

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
    public Admin save(MultipartFile file, AdminRequest administratorRequest) {

        if (repositoryPort.existsByDni(administratorRequest.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado.");
        }

        if (administratorRequest.getPhone() != null && repositoryPort.existsByPhone(administratorRequest.getPhone())) {
            throw new DuplicateResourceException("El número de teléfono ya está registrado.");
        }


        User user = userUseCase.save( administratorRequest.getEmail(), administratorRequest.getUsername(), administratorRequest.getPassword(), "ROLE_ADMINISTRATOR");

        String fileName = fileUseCase.storeFile(file, "admin");

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/assets/")
                .path(fileName)
                .toUriString();

        Admin administrator = Admin.builder()
                .firstName(administratorRequest.getFirstName())
                .middleName(administratorRequest.getMiddleName())
                .paternalLastName(administratorRequest.getPaternalLastName())
                .maternalLastName(administratorRequest.getMaternalLastName())
                .dni(administratorRequest.getDni())
                .phone(administratorRequest.getPhone())
                .birthDate(administratorRequest.getBirthDate())
                .profile(fileUrl)
                .gender(administratorRequest.getGender())
                .nationality(administratorRequest.getNationality())
                .status(UserStatus.ACTIVE)
                .user(user)
                .build();

        return repositoryPort.save(administrator);
    }

    @Override
    public Admin update(Long id, AdminRequest administratorRequest) {
        return null;
    }

    @Override
    public Page<Admin> getByStatus(UserStatus status, String search, Pageable pageable) {
        return null;
    }

    @Override
    public Admin deactivate(Long id) {
        return null;
    }

    @Override
    public Admin activate(Long id) {
        return null;
    }


}
