package com.san_andres.backend.admin.infrastructure.persistence.adapter;

import com.san_andres.backend.admin.application.dto.response.AdminResponse;
import com.san_andres.backend.admin.domain.model.Admin;
import com.san_andres.backend.admin.domain.port.repository.AdminRepositoryPort;
import com.san_andres.backend.admin.infrastructure.persistence.entity.AdminEntity;
import com.san_andres.backend.admin.infrastructure.persistence.mapper.AdminMapper;
import com.san_andres.backend.admin.infrastructure.persistence.repository.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminRepositoryAdapter implements AdminRepositoryPort {

    private final JpaAdminRepository repository;
    private final AdminMapper mapper;

    @Override
    public Optional<Admin> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Admin> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Admin save(Admin administrator) {
        AdminEntity entity = mapper.toEntity(administrator);
        AdminEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }


    @Override
    public boolean existsByDni(String dni) {
        return repository.existsByDni(dni);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Override
    public Page<AdminResponse> getByStatus(String status, String search, Pageable pageable){
        return repository.searchByStatus(status, search, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Optional<AdminResponse> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toResponse);
    }

    @Override
    public List<AdminResponse> search(String search, int limit) {

        Pageable pageable = PageRequest.of(0, limit);

        return repository.searchActive(search, pageable)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<AdminResponse> findRandom(int limit) {

        return repository.findRandom(limit)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}
