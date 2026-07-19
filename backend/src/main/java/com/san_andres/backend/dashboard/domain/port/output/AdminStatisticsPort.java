package com.san_andres.backend.dashboard.domain.port.output;

import com.san_andres.backend.dashboard.infrastructure.port.output.persistence.projection.StatisticProjection;

import java.util.List;

public interface AdminStatisticsPort {

    StatisticProjection countAdministrators();

    StatisticProjection countActiveAdministrators();

    StatisticProjection countInactiveAdministrators();

    StatisticProjection countRegisteredLastMonth();

    List<StatisticProjection> countByGender();

    List<StatisticProjection> countRegistersLastSixMonths();

    List<StatisticProjection> getStatusStatistics();

}