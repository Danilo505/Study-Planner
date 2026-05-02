package Study.Planner.project.topic.dto;

import Study.Planner.project.topic.entity.TopicDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTopicRequest(

        @NotNull
        UUID subjectId,

        @NotBlank
        @Size(min = 2, max = 150)
        String name,

        @Size(max = 1000)
        String description,

        Integer estimatedHours,

        @NotNull
        TopicDifficulty difficulty
) {
}
