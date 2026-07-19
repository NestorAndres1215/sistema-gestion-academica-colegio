package com.san_andres.backend.dashboard.application.service;

import com.san_andres.backend.dashboard.domain.port.repository.AdminStatisticsPort;
import com.san_andres.backend.dashboard.domain.port.usecase.AdminStatisticsUseCase;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.StatisticProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService implements AdminStatisticsUseCase {

    private final AdminStatisticsPort adminStatisticsPort;

    @Override
    public StatisticProjection getTotalAdministrators() {
        return adminStatisticsPort.countAdministrators();
    }

    @Override
    public StatisticProjection getActiveAdministrators() {
        return adminStatisticsPort.countActiveAdministrators();
    }

    @Override
    public StatisticProjection getInactiveAdministrators() {
        return adminStatisticsPort.countInactiveAdministrators();
    }

    @Override
    public StatisticProjection getRegisteredLastMonth() {
        return adminStatisticsPort.countRegisteredLastMonth();
    }

    @Override
    public List<StatisticProjection> getGenderStatistics() {
        return adminStatisticsPort.countByGender();
    }

    @Override
    public List<StatisticProjection> getLastSixMonthsRegisters() {
        return adminStatisticsPort.countRegistersLastSixMonths();
    }

    @Override
    public List<StatisticProjection> getStatusStatistics() {
        return adminStatisticsPort.getStatusStatistics();
    }

}