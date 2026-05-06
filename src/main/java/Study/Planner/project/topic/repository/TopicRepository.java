package Study.Planner.project.topic.repository;

import Study.Planner.project.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findAllBySubjectUserEmailOrderByCreatedAtDesc(String email);

    List<Topic> findAllBySubjectIdAndSubjectUserEmail(UUID subjectId, String email);

    List<Topic> findAllBySubjectUserEmailAndCompletedFalse(String email);

    Optional<Topic> findByIdAndSubjectUserEmail(UUID id, String email);
}
