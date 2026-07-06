package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdminRepositoryPort {

    Optional<Admin> findById(Long id);

    List<Admin> findAll();

    Admin save(Admin administrator);

    boolean existsByDni(String dni);

    boolean existsByPhone(String phone);

    Page<AdminResponse> getByStatus(String status, String search, Pageable pageable);

}
