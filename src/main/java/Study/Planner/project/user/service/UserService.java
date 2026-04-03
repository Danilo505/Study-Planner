package Study.Planner.project.user.service;

import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.saveAndFlush(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

   public void deleteUserByEmail(String email) {
        userRepository.delete(findUserByEmail(email));
   }

   public void updateUserById(UUID id, User user) {
        User userEntity = userRepository.findById(id).orElseThrow();

        User userUpdate = User.builder()
                .email(user.getEmail() != null? user.getEmail() : userEntity.getEmail())
                .name(user.getName() != null? user.getName() : userEntity.getName())
                .id(userEntity.getId())
                .build();

        userRepository.saveAndFlush(userUpdate);
   }
}
