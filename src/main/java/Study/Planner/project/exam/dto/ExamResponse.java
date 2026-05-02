package Study.Planner.project.exam.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExamResponse(
        UUID id,
        UUID subjectId,
        String subjectName,
        String title,
        LocalDate examDate,
        Integer weight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
