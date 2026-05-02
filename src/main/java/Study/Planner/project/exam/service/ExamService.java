package Study.Planner.project.exam.service;

import Study.Planner.project.exam.dto.CreateExamRequest;
import Study.Planner.project.exam.dto.ExamResponse;
import Study.Planner.project.exam.dto.UpdateExamRequest;
import Study.Planner.project.exam.entity.Exam;
import Study.Planner.project.exam.repository.ExamRepository;
import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.subject.entity.Subject;
import Study.Planner.project.subject.repository.SubjectRepository;
import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public ExamResponse create(String email, CreateExamRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));

        Subject subject = subjectRepository.findByIdAndUserEmail(request.subjectId(), email)
                .orElseThrow(() -> new BusinessException("Subject not found"));

        Exam exam = Exam.builder()
                .user(user)
                .subject(subject)
                .title(request.title())
                .examDate(request.examDate())
                .weight(request.weight())
                .build();

        return toResponse(examRepository.save(exam));
    }

    public List<ExamResponse> findAll(String email) {
        return examRepository.findAllByUserEmailOrderByExamDateAsc(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ExamResponse> findUpcoming(String email) {
        return examRepository
                .findAllByUserEmailAndExamDateGreaterThanEqualOrderByExamDateAsc(
                        email,
                        LocalDate.now()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ExamResponse> findBySubject(String email, UUID subjectId) {
        return examRepository.findAllBySubjectIdAndUserEmailOrderByExamDateAsc(subjectId, email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ExamResponse findById(String email, UUID id) {
        return toResponse(findOwnedExam(email, id));
    }

    public ExamResponse update(String email, UUID id, UpdateExamRequest request) {
        Exam exam = findOwnedExam(email, id);

        if (request.subjectId() != null) {
            Subject subject = subjectRepository.findByIdAndUserEmail(request.subjectId(), email)
                    .orElseThrow(() -> new BusinessException("Subject not found"));

            exam.setSubject(subject);
        }

        if (request.title() != null && !request.title().isBlank()) {
            exam.setTitle(request.title());
        }

        if (request.examDate() != null) {
            exam.setExamDate(request.examDate());
        }

        if (request.weight() != null) {
            exam.setWeight(request.weight());
        }

        return toResponse(examRepository.save(exam));
    }

    public void delete(String email, UUID id) {
        examRepository.delete(findOwnedExam(email, id));
    }

    private Exam findOwnedExam(String email, UUID id) {
        return examRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new BusinessException("Exam not found"));
    }

    private ExamResponse toResponse(Exam exam) {
        return new ExamResponse(
                exam.getId(),
                exam.getSubject().getId(),
                exam.getSubject().getName(),
                exam.getTitle(),
                exam.getExamDate(),
                exam.getWeight(),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }
}
