package Study.Planner.project.topic.dto;

import Study.Planner.project.topic.entity.TopicDifficulty;
import jakarta.validation.constraints.Size;

public record UpdateTopicRequest(

        @Size(min = 2, max = 150)
        String name,

        String description,

        Integer estimatedHours,

        TopicDifficulty difficulty
) {
}
