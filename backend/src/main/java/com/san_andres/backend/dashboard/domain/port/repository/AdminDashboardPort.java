package com.san_andres.backend.dashboard.domain.port.repository;

import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;

import java.util.List;

public interface AdminDashboardPort {

    DashboardProjection countAdministrators();

    DashboardProjection countActiveAdministrators();

    DashboardProjection countInactiveAdministrators();

    DashboardProjection countRegisteredLastMonth();

    List<DashboardProjection> countByGender();

    List<DashboardProjection> countRegistersLastSixMonths();

    List<DashboardProjection> getStatusStatistics();

}