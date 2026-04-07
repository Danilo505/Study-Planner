package Study.Planner.project.user.controller;

import Study.Planner.project.user.dto.ChangePasswordRequest;
import Study.Planner.project.user.dto.UpdateUserRequest;
import Study.Planner.project.user.dto.UserResponse;
import Study.Planner.project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            Authentication authentication,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateCurrentUser(authentication.getName(), request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
