package com.san_andres.backend.dashboard.infrastructure.port.output.adapter;

import com.san_andres.backend.dashboard.domain.port.output.AdminStatisticsPort;
import com.san_andres.backend.dashboard.infrastructure.port.output.persistence.projection.StatisticProjection;
import com.san_andres.backend.admin.infrastructure.adapter.output.persistence.repository.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminStatisticsAdapter implements AdminStatisticsPort {

    private final JpaAdminRepository adminRepository;

    @Override
    public StatisticProjection countAdministrators() {
        return adminRepository.countAdministrators();
    }

    @Override
    public StatisticProjection countActiveAdministrators() {
        return adminRepository.countActiveAdministrators();
    }

    @Override
    public StatisticProjection countInactiveAdministrators() {
        return adminRepository.countInactiveAdministrators();
    }

    @Override
    public StatisticProjection countRegisteredLastMonth() {
        return adminRepository.countRegisteredLastMonth();
    }

    @Override
    public List<StatisticProjection> countByGender() {
        return adminRepository.countByGender();
    }

    @Override
    public List<StatisticProjection> countRegistersLastSixMonths() {
        return adminRepository.countRegistersLastSixMonths();
    }

    @Override
    public List<StatisticProjection> getStatusStatistics() {
        return adminRepository.getStatusStatistics();
    }

}