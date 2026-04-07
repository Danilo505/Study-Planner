package Study.Planner.project.user.service;

import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.user.dto.ChangePasswordRequest;
import Study.Planner.project.user.dto.UpdateUserRequest;
import Study.Planner.project.user.dto.UserResponse;
import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));

        return toResponse(user);
    }

    public UserResponse updateCurrentUser(String currentEmail, UpdateUserRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (request.email() != null
                && !request.email().equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use");
        }

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
        }

        userRepository.save(user);

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public void changePassword(String currentEmail, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BusinessException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
