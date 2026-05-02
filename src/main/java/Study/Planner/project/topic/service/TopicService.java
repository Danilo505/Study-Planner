package Study.Planner.project.topic.service;

import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.subject.entity.Subject;
import Study.Planner.project.subject.repository.SubjectRepository;
import Study.Planner.project.topic.dto.*;
import Study.Planner.project.topic.entity.Topic;
import Study.Planner.project.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicResponse create(String email, CreateTopicRequest request) {
        Subject subject = subjectRepository
                .findByIdAndUserEmail(request.subjectId(), email)
                .orElseThrow(() -> new BusinessException("Subject not found"));

        Topic topic = Topic.builder()
                .subject(subject)
                .name(request.name())
                .description(request.description())
                .estimatedHours(request.estimatedHours())
                .difficulty(request.difficulty())
                .build();

        return toResponse(topicRepository.save(topic));
    }

    public List<TopicResponse> findAll(String email) {
        return topicRepository.findAllBySubjectUserEmailOrderByCreatedAtDesc(email)
                .stream().map(this::toResponse).toList();
    }

    public List<TopicResponse> findBySubject(String email, UUID subjectId) {
        return topicRepository.findAllBySubjectIdAndSubjectUserEmail(subjectId, email)
                .stream().map(this::toResponse).toList();
    }

    public TopicResponse update(String email, UUID id, UpdateTopicRequest request) {
        Topic topic = findOwnedTopic(email, id);

        if (request.name() != null) topic.setName(request.name());
        if (request.description() != null) topic.setDescription(request.description());
        if (request.estimatedHours() != null) topic.setEstimatedHours(request.estimatedHours());
        if (request.difficulty() != null) topic.setDifficulty(request.difficulty());

        return toResponse(topicRepository.save(topic));
    }

    public void toggleComplete(String email, UUID id) {
        Topic topic = findOwnedTopic(email, id);
        topic.setCompleted(!topic.getCompleted());
        topicRepository.save(topic);
    }

    public void delete(String email, UUID id) {
        topicRepository.delete(findOwnedTopic(email, id));
    }

    private Topic findOwnedTopic(String email, UUID id) {
        return topicRepository.findByIdAndSubjectUserEmail(id, email)
                .orElseThrow(() -> new BusinessException("Topic not found"));
    }

    private TopicResponse toResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getSubject().getId(),
                topic.getName(),
                topic.getDescription(),
                topic.getEstimatedHours(),
                topic.getDifficulty(),
                topic.getCompleted(),
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }
}
