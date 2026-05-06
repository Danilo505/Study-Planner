package Study.Planner.project.studyplan.repository;

import Study.Planner.project.studyplan.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    List<StudySession> findAllByStudyPlanIdOrderByPlannedDateAsc(UUID studyPlanId);

    List<StudySession> findAllByStudyPlanUserEmailAndPlannedDateOrderByPlannedDateAsc(
            String email,
            LocalDate plannedDate
    );

    Optional<StudySession> findByIdAndStudyPlanUserEmail(UUID id, String email);

    Long countByStudyPlanUserEmailAndCompletedTrue(String email);

    Long countByStudyPlanUserEmailAndCompletedFalse(String email);

    List<StudySession> findAllByStudyPlanUserEmail(String email);
}
