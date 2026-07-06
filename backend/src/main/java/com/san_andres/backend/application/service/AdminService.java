package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.admin.AdminRequest;
import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.exceptions.DuplicateResourceException;
import com.san_andres.backend.domain.exceptions.ResourceNotFoundException;
import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.domain.models.Role;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

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
        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator no encontrado"));

        if (!existing.getDni().equals(administratorRequest.getDni()) &&
                repositoryPort.existsByDni(administratorRequest.getDni())) {
            throw new DuplicateResourceException("Dni ya registrado .");
        }

        if (administratorRequest.getPhone() != null &&
                !administratorRequest.getPhone().equals(existing.getPhone()) &&
                repositoryPort.existsByPhone(administratorRequest.getPhone())) {
            throw new DuplicateResourceException("Telefono ya registrado");
        }

        existing.setFirstName(administratorRequest.getFirstName());
        existing.setMiddleName(administratorRequest.getMiddleName());
        existing.setPaternalLastName(administratorRequest.getPaternalLastName());
        existing.setMaternalLastName(administratorRequest.getMaternalLastName());
        existing.setDni(administratorRequest.getDni());
        existing.setPhone(administratorRequest.getPhone());
        existing.setBirthDate(administratorRequest.getBirthDate());
        existing.setProfile(administratorRequest.getProfile());
        existing.setGender(administratorRequest.getGender());
        existing.setNationality(administratorRequest.getNationality());

        return repositoryPort.save(existing);
    }

    @Override
    public Page<AdminResponse> getByStatus(UserStatus status, String search, Pageable pageable) {
        return repositoryPort.getByStatus(status, search, pageable);
    }

    @Override
    public Admin deactivate(Long id) {
        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator no encontrado"));
        userUseCase.deactivateUser(existing.getUser().getId());
        existing.setStatus(UserStatus.INACTIVE);
        return repositoryPort.save(existing);
    }

    @Override
    public Admin activate(Long id) {
        Admin existing = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator no encontrado"));
        userUseCase.activateUser(existing.getUser().getId());
        existing.setStatus(UserStatus.ACTIVE);
        return repositoryPort.save(existing);
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0; // o null si cambias a Integer
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
