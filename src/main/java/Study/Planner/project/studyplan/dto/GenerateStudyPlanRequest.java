package Study.Planner.project.studyplan.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GenerateStudyPlanRequest(

        @NotNull
        @FutureOrPresent
        LocalDate startDate,

        @NotNull
        @FutureOrPresent
        LocalDate endDate,

        @NotNull
        @Min(1)
        @Max(12)
        Integer hoursPerDay
) {
}
