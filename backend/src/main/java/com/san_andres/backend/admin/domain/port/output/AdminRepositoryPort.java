package com.san_andres.backend.admin.domain.port.output;

import com.san_andres.backend.admin.application.dto.response.AdminResponse;
import com.san_andres.backend.admin.domain.model.Admin;
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

    Optional<AdminResponse> findByEmail(String email);

    List<AdminResponse> search(String search, int limit);

    List<AdminResponse> findRandom(int limit);
    
}
