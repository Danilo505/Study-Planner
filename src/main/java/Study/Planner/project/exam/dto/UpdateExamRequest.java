package Study.Planner.project.exam.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateExamRequest(

        UUID subjectId,

        @Size(min = 2, max = 150)
        String title,

        @FutureOrPresent
        LocalDate examDate,

        @Min(1)
        @Max(10)
        Integer weight
) {
}
