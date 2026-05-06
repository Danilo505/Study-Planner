package Study.Planner.project.dashboard.controller;

import Study.Planner.project.dashboard.dto.DashboardResponse;
import Study.Planner.project.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                dashboardService.getDashboard(authentication.getName())
        );
    }
}
