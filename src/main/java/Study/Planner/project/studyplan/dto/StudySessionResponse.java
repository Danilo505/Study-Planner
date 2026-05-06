package Study.Planner.project.studyplan.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudySessionResponse(
        UUID id,
        UUID topicId,
        String topicName,
        UUID subjectId,
        String subjectName,
        LocalDate plannedDate,
        Integer plannedHours,
        Boolean completed,
        LocalDateTime completedAt
) {
}