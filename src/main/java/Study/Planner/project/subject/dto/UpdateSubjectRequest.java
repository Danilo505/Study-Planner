package Study.Planner.project.subject.dto;

import Study.Planner.project.subject.entity.SubjectDifficulty;
import jakarta.validation.constraints.Size;

public record UpdateSubjectRequest(

        @Size(min = 2, max = 120)
        String name,

        @Size(max = 1000)
        String description,

        SubjectDifficulty difficulty
) {
}
