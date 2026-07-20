package com.san_andres.backend.dashboard.domain.port.usecase;

import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import java.util.List;

public interface AdminDashboardUseCase {

    DashboardProjection getTotalAdministrators();

    DashboardProjection getActiveAdministrators();

    DashboardProjection getInactiveAdministrators();

    DashboardProjection getRegisteredLastMonth();

    List<DashboardProjection> getGenderStatistics();

    List<DashboardProjection> getLastSixMonthsRegisters();

    List<DashboardProjection> getStatusStatistics();

}