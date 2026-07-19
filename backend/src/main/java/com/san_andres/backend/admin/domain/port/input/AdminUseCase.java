package com.san_andres.backend.admin.domain.port.input;

import com.san_andres.backend.admin.application.dto.request.AdminRequest;
import com.san_andres.backend.admin.application.dto.response.AdminResponse;
import com.san_andres.backend.report.application.dto.reponse.ImportResult;
import com.san_andres.backend.admin.domain.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface AdminUseCase {

    Admin findById(Long id);

    List<Admin> findAll();

    Admin save( AdminRequest administratorRequest);

    Admin update(Long id,MultipartFile file, AdminRequest administratorRequest);

    Page<AdminResponse> getByStatus(String status, String search, Pageable pageable);

    Admin deactivate(Long id);

    Admin activate(Long id);

    Admin blocked(Long id);

    Optional<AdminResponse> findByEmail(String email);

    List<AdminResponse> search(String search);

    List<Admin> saveAll(List<AdminRequest> requests);

    ImportResult importExcel(MultipartFile file) ;
}
