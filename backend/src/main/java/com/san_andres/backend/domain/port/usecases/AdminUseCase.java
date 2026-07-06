package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.admin.AdminRequest;
import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface AdminUseCase {

    Admin findById(Long id);

    List<Admin> findAll();

    Admin save(MultipartFile file, AdminRequest administratorRequest);

    Admin update(Long id, AdminRequest administratorRequest);

    Page<AdminResponse> getByStatus(UserStatus status, String search, Pageable pageable);

    Admin deactivate(Long id);

    Admin activate(Long id);


}
