package Study.Planner.project.studyplan.repository;

import Study.Planner.project.studyplan.entity.StudyPlan;
import Study.Planner.project.studyplan.entity.StudyPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, UUID> {

    Optional<StudyPlan> findFirstByUserEmailAndStatusOrderByCreatedAtDesc(
            String email,
            StudyPlanStatus status
    );
}
