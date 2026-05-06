package Study.Planner.project.studyplan.dto;

import Study.Planner.project.studyplan.entity.StudyPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StudyPlanResponse(
        UUID id,
        LocalDate startDate,
        LocalDate endDate,
        Integer hoursPerDay,
        StudyPlanStatus status,
        LocalDateTime createdAt,
        List<StudySessionResponse> sessions
) {
}
