package Study.Planner.project.auth.Controller;

import Study.Planner.project.auth.dto.AuthResponse;
import Study.Planner.project.auth.dto.LoginRequest;
import Study.Planner.project.auth.dto.RegisterRequest;
import Study.Planner.project.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest data) {
        AuthResponse response = authService.login(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest data) {
        authService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}