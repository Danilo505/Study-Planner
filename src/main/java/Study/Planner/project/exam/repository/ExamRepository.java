package Study.Planner.project.exam.repository;

import Study.Planner.project.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamRepository extends JpaRepository<Exam, UUID> {

    List<Exam> findAllByUserEmailOrderByExamDateAsc(String email);

    List<Exam> findAllByUserEmailAndExamDateGreaterThanEqualOrderByExamDateAsc(
            String email,
            LocalDate date
    );

    List<Exam> findAllBySubjectIdAndUserEmailOrderByExamDateAsc(UUID subjectId, String email);

    Optional<Exam> findByIdAndUserEmail(UUID id, String email);
}
