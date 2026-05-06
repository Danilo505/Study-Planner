package Study.Planner.project.dashboard.dto;

import java.time.LocalDate;

public record NextExamResponse(
        String title,
        String subject,
        LocalDate date
) {
}
