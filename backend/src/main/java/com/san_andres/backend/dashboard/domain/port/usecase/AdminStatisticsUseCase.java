package com.san_andres.backend.dashboard.domain.port.usecase;

import com.san_andres.backend.dashboard.infrastructure.persistence.projection.StatisticProjection;
import java.util.List;

public interface AdminStatisticsUseCase {

    StatisticProjection getTotalAdministrators();

    StatisticProjection getActiveAdministrators();

    StatisticProjection getInactiveAdministrators();

    StatisticProjection getRegisteredLastMonth();

    List<StatisticProjection> getGenderStatistics();

    List<StatisticProjection> getLastSixMonthsRegisters();

    List<StatisticProjection> getStatusStatistics();

}