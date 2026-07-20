package com.san_andres.backend.dashboard.application.service;

import com.san_andres.backend.dashboard.domain.port.repository.AdminDashboardPort;
import com.san_andres.backend.dashboard.domain.port.usecase.AdminDashboardUseCase;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService implements AdminDashboardUseCase {

    private final AdminDashboardPort adminStatisticsPort;

    @Override
    public DashboardProjection getTotalAdministrators() {
        return adminStatisticsPort.countAdministrators();
    }

    @Override
    public DashboardProjection getActiveAdministrators() {
        return adminStatisticsPort.countActiveAdministrators();
    }

    @Override
    public DashboardProjection getInactiveAdministrators() {
        return adminStatisticsPort.countInactiveAdministrators();
    }

    @Override
    public DashboardProjection getRegisteredLastMonth() {
        return adminStatisticsPort.countRegisteredLastMonth();
    }

    @Override
    public List<DashboardProjection> getGenderStatistics() {
        return adminStatisticsPort.countByGender();
    }

    @Override
    public List<DashboardProjection> getLastSixMonthsRegisters() {
        return adminStatisticsPort.countRegistersLastSixMonths();
    }

    @Override
    public List<DashboardProjection> getStatusStatistics() {
        return adminStatisticsPort.getStatusStatistics();
    }

}