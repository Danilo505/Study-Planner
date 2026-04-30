package Study.Planner.project.subject.service;

import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.subject.dto.CreateSubjectRequest;
import Study.Planner.project.subject.dto.SubjectResponse;
import Study.Planner.project.subject.dto.UpdateSubjectRequest;
import Study.Planner.project.subject.entity.Subject;
import Study.Planner.project.subject.repository.SubjectRepository;
import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectResponse create(String userEmail, CreateSubjectRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (subjectRepository.existsByNameIgnoreCaseAndUserEmail(request.name(), userEmail)) {
            throw new BusinessException("Subject already exists");
        }

        Subject subject = Subject.builder()
                .user(user)
                .name(request.name())
                .description(request.description())
                .difficulty(request.difficulty())
                .build();

        Subject savedSubject = subjectRepository.save(subject);

        return toResponse(savedSubject);
    }

    public List<SubjectResponse> findAll(String userEmail) {
        return subjectRepository.findAllByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SubjectResponse findById(String userEmail, UUID id) {
        Subject subject = findSubjectBelongingToUser(id, userEmail);
        return toResponse(subject);
    }

    public SubjectResponse update(String userEmail, UUID id, UpdateSubjectRequest request) {
        Subject subject = findSubjectBelongingToUser(id, userEmail);

        if (request.name() != null
                && !request.name().equalsIgnoreCase(subject.getName())
                && subjectRepository.existsByNameIgnoreCaseAndUserEmailAndIdNot(request.name(), userEmail, id)) {
            throw new BusinessException("Subject already exists");
        }

        if (request.name() != null && !request.name().isBlank()) {
            subject.setName(request.name());
        }

        if (request.description() != null) {
            subject.setDescription(request.description());
        }

        if (request.difficulty() != null) {
            subject.setDifficulty(request.difficulty());
        }

        Subject updatedSubject = subjectRepository.save(subject);

        return toResponse(updatedSubject);
    }

    public void delete(String userEmail, UUID id) {
        Subject subject = findSubjectBelongingToUser(id, userEmail);
        subjectRepository.delete(subject);
    }

    private Subject findSubjectBelongingToUser(UUID id, String userEmail) {
        return subjectRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new BusinessException("Subject not found"));
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getDescription(),
                subject.getDifficulty(),
                subject.getCreatedAt(),
                subject.getUpdatedAt()
        );
    }
}
