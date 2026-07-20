package com.san_andres.backend.dashboard.infrastructure.persistence.adapter;

import com.san_andres.backend.dashboard.domain.port.repository.AdminDashboardPort;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import com.san_andres.backend.admin.infrastructure.persistence.repository.JpaAdminRepository;
import com.san_andres.backend.dashboard.infrastructure.persistence.repository.JpaAdminDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminDashboardAdapter implements AdminDashboardPort {

    private final JpaAdminDashboardRepository adminRepository;

    @Override
    public DashboardProjection countAdministrators() {
        return adminRepository.countAdministrators();
    }

    @Override
    public DashboardProjection countActiveAdministrators() {
        return adminRepository.countActiveAdministrators();
    }

    @Override
    public DashboardProjection countInactiveAdministrators() {
        return adminRepository.countInactiveAdministrators();
    }

    @Override
    public DashboardProjection countRegisteredLastMonth() {
        return adminRepository.countRegisteredLastMonth();
    }

    @Override
    public List<DashboardProjection> countByGender() {
        return adminRepository.countByGender();
    }

    @Override
    public List<DashboardProjection> countRegistersLastSixMonths() {
        return adminRepository.countRegistersLastSixMonths();
    }

    @Override
    public List<DashboardProjection> getStatusStatistics() {
        return adminRepository.getStatusStatistics();
    }

}