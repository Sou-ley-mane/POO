package com.gie.fleet.controller;

import com.gie.fleet.dto.dashboard.DashboardStatsDto;
import com.gie.fleet.service.DashboardService;
import com.gie.fleet.tenant.TenantContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Tableau de bord")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardStatsDto> obtenirStats() {
        return ResponseEntity.ok(dashboardService.calculer(TenantContext.getGieId()));
    }
}
