package com.san_andres.backend.dashboard.infrastructure.controller;

import com.san_andres.backend.dashboard.domain.port.usecase.AdminStatisticsUseCase;
import com.san_andres.backend.dashboard.infrastructure.persistence.projection.StatisticProjection;
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
public class AdminStatisticsController {

    private final AdminStatisticsUseCase adminStatisticsUseCase;

    @GetMapping("/total")
    public StatisticProjection total() {
        return adminStatisticsUseCase.getTotalAdministrators();
    }

    @GetMapping("/active")
    public StatisticProjection active() {
        return adminStatisticsUseCase.getActiveAdministrators();
    }

    @GetMapping("/inactive")
    public StatisticProjection inactive() {
        return adminStatisticsUseCase.getInactiveAdministrators();
    }

    @GetMapping("/last-month")
    public StatisticProjection lastMonth() {
        return adminStatisticsUseCase.getRegisteredLastMonth();
    }

    @GetMapping("/gender")
    public List<StatisticProjection> gender() {
        return adminStatisticsUseCase.getGenderStatistics();
    }

    @GetMapping("/registers-six-months")
    public List<StatisticProjection> registersSixMonths() {
        return adminStatisticsUseCase.getLastSixMonthsRegisters();
    }

    @GetMapping("/status")
    public List<StatisticProjection> status() {
        return adminStatisticsUseCase.getStatusStatistics();
    }

}