package Study.Planner.project.auth.service;

import Study.Planner.project.auth.dto.AuthResponse;
import Study.Planner.project.auth.dto.LoginRequest;
import Study.Planner.project.auth.dto.RegisterRequest;
import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.shared.security.JwtService;
import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.entity.UserRole;
import Study.Planner.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest data) {
        var usernamePassword =
                new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = jwtService.generateToken((User) auth.getPrincipal());

        return new AuthResponse(token);
    }

    public void register(RegisterRequest data) {
        if (userRepository.existsByEmail(data.email())) {
            throw new BusinessException("Email already registered");
        }

        User newUser = User.builder()
                .name(data.name())
                .email(data.email())
                .passwordHash(passwordEncoder.encode(data.password()))
                .role(UserRole.USER)
                .build();

        userRepository.save(newUser);
    }
}