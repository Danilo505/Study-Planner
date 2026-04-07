package Study.Planner.project.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 3, max = 120)
        String name,

        @Email
        @Size(max = 150)
        String email
) {
}
