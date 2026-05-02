package Study.Planner.project.topic.dto;

import Study.Planner.project.topic.entity.TopicDifficulty;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicResponse(
        UUID id,
        UUID subjectId,
        String name,
        String description,
        Integer estimatedHours,
        TopicDifficulty difficulty,
        Boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
