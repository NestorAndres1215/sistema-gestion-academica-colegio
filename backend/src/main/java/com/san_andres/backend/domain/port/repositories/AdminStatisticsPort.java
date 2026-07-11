package com.san_andres.backend.domain.port.repositories;

import com.san_andres.backend.infrastructure.persistence.projection.StatisticProjection;

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