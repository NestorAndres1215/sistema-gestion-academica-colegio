package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.port.repositories.AdminReportPort;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminReportAdapter implements AdminReportPort {

    private final JpaAdminRepository repository;

    @Override
    public List<AdministratorReportProjection> findForReport(String status){
        return repository.findForReport(status);
    }

    @Override
    public List<AdministratorReportProjection> findForReportId(Long id) {
        return repository.findForReportId(id);
    }


}