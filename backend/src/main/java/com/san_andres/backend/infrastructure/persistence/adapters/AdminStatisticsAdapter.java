package com.san_andres.backend.infrastructure.persistence.adapters;

import com.san_andres.backend.domain.port.repositories.AdminStatisticsPort;
import com.san_andres.backend.infrastructure.persistence.projection.PercentageStatisticProjection;
import com.san_andres.backend.infrastructure.persistence.projection.StatisticProjection;
import com.san_andres.backend.infrastructure.persistence.repositories.JpaAdminRepository;
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
    public List<PercentageStatisticProjection> getStatusStatistics() {
        return adminRepository.getStatusStatistics();
    }

}