package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.infrastructure.persistence.projection.PercentageStatisticProjection;
import com.san_andres.backend.infrastructure.persistence.projection.StatisticProjection;

import java.util.List;

public interface AdminStatisticsUseCase {

    StatisticProjection getTotalAdministrators();

    StatisticProjection getActiveAdministrators();

    StatisticProjection getInactiveAdministrators();

    StatisticProjection getRegisteredLastMonth();

    List<StatisticProjection> getGenderStatistics();

    List<StatisticProjection> getLastSixMonthsRegisters();

    List<PercentageStatisticProjection> getStatusStatistics();

}