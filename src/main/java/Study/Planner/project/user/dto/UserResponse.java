package Study.Planner.project.user.dto;

import Study.Planner.project.user.entity.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role
) {
}
