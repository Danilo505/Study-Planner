package Study.Planner.project.subject.dto;

import Study.Planner.project.subject.entity.SubjectDifficulty;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubjectResponse(
        UUID id,
        String name,
        String description,
        SubjectDifficulty difficulty,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
