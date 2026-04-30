package Study.Planner.project.subject.dto;

import Study.Planner.project.subject.entity.SubjectDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSubjectRequest(

        @NotBlank
        @Size(min = 2, max = 120)
        String name,

        @Size(max = 1000)
        String description,

        @NotNull
        SubjectDifficulty difficulty
) {
}
