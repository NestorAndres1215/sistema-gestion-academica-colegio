package com.san_andres.backend.dashboard.infrastructure.controller;

import com.san_andres.backend.dashboard.domain.port.usecase.AdminDashboardUseCase;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.DashboardProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard/admin")
@RequiredArgsConstructor
@Tag(name = "Administrator Dashboard")
public class AdminDashboardController {

    private final AdminDashboardUseCase adminStatisticsUseCase;

    @GetMapping("/total")
    public DashboardProjection total() {
        return adminStatisticsUseCase.getTotalAdministrators();
    }

    @GetMapping("/active")
    public DashboardProjection active() {
        return adminStatisticsUseCase.getActiveAdministrators();
    }

    @GetMapping("/inactive")
    public DashboardProjection inactive() {
        return adminStatisticsUseCase.getInactiveAdministrators();
    }

    @GetMapping("/last-month")
    public DashboardProjection lastMonth() {
        return adminStatisticsUseCase.getRegisteredLastMonth();
    }

    @GetMapping("/gender")
    public List<DashboardProjection> gender() {
        return adminStatisticsUseCase.getGenderStatistics();
    }

    @GetMapping("/registers-six-months")
    public List<DashboardProjection> registersSixMonths() {
        return adminStatisticsUseCase.getLastSixMonthsRegisters();
    }

    @GetMapping("/status")
    public List<DashboardProjection> status() {
        return adminStatisticsUseCase.getStatusStatistics();
    }

}