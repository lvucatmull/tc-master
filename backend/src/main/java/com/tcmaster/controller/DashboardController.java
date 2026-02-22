package com.tcmaster.controller;

import com.tcmaster.dto.DashboardDtos;
import com.tcmaster.service.DashboardService;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{id}")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardDtos.DashboardResponse getDashboard(@PathVariable Long id) {
        return dashboardService.getDashboard(id);
    }

    @GetMapping("/stats")
    public DashboardDtos.StatsResponse getStats(@PathVariable Long id) {
        return dashboardService.getStats(id);
    }

    @GetMapping("/reports")
    public List<DashboardDtos.ReportRow> getReports(@PathVariable Long id) {
        return dashboardService.getReports(id);
    }

    @GetMapping("/reports/csv")
    public ResponseEntity<String> exportCsv(@PathVariable Long id) {
        String csv = dashboardService.exportCsv(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project-" + id + "-report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }
}
