package Study.Planner.project.exam.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateExamRequest(

        @NotNull
        UUID subjectId,

        @NotBlank
        @Size(min = 2, max = 150)
        String title,

        @NotNull
        @FutureOrPresent
        LocalDate examDate,

        @NotNull
        @Min(1)
        @Max(10)
        Integer weight
) {
}
