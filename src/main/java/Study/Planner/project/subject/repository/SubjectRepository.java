package Study.Planner.project.subject.repository;

import Study.Planner.project.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    List<Subject> findAllByUserEmailOrderByCreatedAtDesc(String email);

    Optional<Subject> findByIdAndUserEmail(UUID id, String email);

    boolean existsByNameIgnoreCaseAndUserEmail(String name, String email);

    boolean existsByNameIgnoreCaseAndUserEmailAndIdNot(String name, String email, UUID id);
}
