package com.san_andres.backend.report.infrastructure.persistence.adapter;

import com.san_andres.backend.report.domain.repository.AdminReportPort;
import com.san_andres.backend.report.infrastructure.persistence.projection.AdministratorReportProjection;
import com.san_andres.backend.admin.infrastructure.persistence.repository.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminReportAdapter implements AdminReportPort {

    private final JpaAdminRepository repository;

    @Override
    public List<AdministratorReportProjection> findForReport(String status) {
        return repository.findForReport(status);
    }

    @Override
    public List<AdministratorReportProjection> findForReportId(Long id) {
        return repository.findForReportId(id);
    }

}