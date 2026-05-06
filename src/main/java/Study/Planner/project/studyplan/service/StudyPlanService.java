package Study.Planner.project.studyplan.service;

import Study.Planner.project.exam.entity.Exam;
import Study.Planner.project.exam.repository.ExamRepository;
import Study.Planner.project.shared.exception.BusinessException;
import Study.Planner.project.studyplan.dto.GenerateStudyPlanRequest;
import Study.Planner.project.studyplan.dto.StudyPlanResponse;
import Study.Planner.project.studyplan.dto.StudySessionResponse;
import Study.Planner.project.studyplan.entity.StudyPlan;
import Study.Planner.project.studyplan.entity.StudyPlanStatus;
import Study.Planner.project.studyplan.entity.StudySession;
import Study.Planner.project.studyplan.repository.StudyPlanRepository;
import Study.Planner.project.studyplan.repository.StudySessionRepository;
import Study.Planner.project.topic.entity.Topic;
import Study.Planner.project.topic.repository.TopicRepository;
import Study.Planner.project.user.entity.User;
import Study.Planner.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudyPlanService {

    private final StudyPlanRepository studyPlanRepository;
    private final StudySessionRepository studySessionRepository;
    private final TopicRepository topicRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    public StudyPlanResponse generate(String email, GenerateStudyPlanRequest request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BusinessException("End date must be after start date");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));

        cancelCurrentActivePlan(email);

        List<Topic> pendingTopics = topicRepository.findAllBySubjectUserEmailAndCompletedFalse(email);

        if (pendingTopics.isEmpty()) {
            throw new BusinessException("There are no pending topics to plan");
        }

        StudyPlan studyPlan = StudyPlan.builder()
                .user(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .hoursPerDay(request.hoursPerDay())
                .status(StudyPlanStatus.ACTIVE)
                .build();

        StudyPlan savedPlan = studyPlanRepository.save(studyPlan);

        List<Topic> orderedTopics = orderTopicsByPriority(email, pendingTopics);
        List<StudySession> sessions = generateSessions(savedPlan, orderedTopics);

        studySessionRepository.saveAll(sessions);

        return toResponse(savedPlan, sessions);
    }

    public StudyPlanResponse getCurrent(String email) {
        StudyPlan plan = studyPlanRepository
                .findFirstByUserEmailAndStatusOrderByCreatedAtDesc(email, StudyPlanStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("No active study plan found"));

        List<StudySession> sessions = studySessionRepository.findAllByStudyPlanIdOrderByPlannedDateAsc(plan.getId());

        return toResponse(plan, sessions);
    }

    public List<StudySessionResponse> getTodaySessions(String email) {
        return studySessionRepository
                .findAllByStudyPlanUserEmailAndPlannedDateOrderByPlannedDateAsc(email, LocalDate.now())
                .stream()
                .map(this::toSessionResponse)
                .toList();
    }

    public void completeSession(String email, UUID sessionId) {
        StudySession session = studySessionRepository.findByIdAndStudyPlanUserEmail(sessionId, email)
                .orElseThrow(() -> new BusinessException("Study session not found"));

        session.setCompleted(true);
        session.setCompletedAt(LocalDateTime.now());

        Topic topic = session.getTopic();
        topic.setCompleted(true);

        studySessionRepository.save(session);
        topicRepository.save(topic);
    }

    private void cancelCurrentActivePlan(String email) {
        studyPlanRepository
                .findFirstByUserEmailAndStatusOrderByCreatedAtDesc(email, StudyPlanStatus.ACTIVE)
                .ifPresent(plan -> {
                    plan.setStatus(StudyPlanStatus.CANCELED);
                    studyPlanRepository.save(plan);
                });
    }

    private List<Topic> orderTopicsByPriority(String email, List<Topic> topics) {
        List<Exam> exams = examRepository.findAllByUserEmailAndExamDateGreaterThanEqualOrderByExamDateAsc(
                email,
                LocalDate.now()
        );

        return topics.stream()
                .sorted((a, b) -> Integer.compare(
                        calculateTopicScore(b, exams),
                        calculateTopicScore(a, exams)
                ))
                .toList();
    }

    private Integer calculateTopicScore(Topic topic, List<Exam> exams) {
        int score = 0;

        score += switch (topic.getDifficulty()) {
            case EASY -> 1;
            case MEDIUM -> 2;
            case HARD -> 3;
        };

        score += topic.getEstimatedHours() != null ? topic.getEstimatedHours() : 1;

        Optional<Exam> nearestExam = exams.stream()
                .filter(exam -> exam.getSubject().getId().equals(topic.getSubject().getId()))
                .findFirst();

        if (nearestExam.isPresent()) {
            Exam exam = nearestExam.get();
            long daysUntilExam = java.time.temporal.ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    exam.getExamDate()
            );

            if (daysUntilExam <= 3) {
                score += 10;
            } else if (daysUntilExam <= 7) {
                score += 7;
            } else if (daysUntilExam <= 14) {
                score += 4;
            } else {
                score += 1;
            }

            score += exam.getWeight();
        }

        return score;
    }

    private List<StudySession> generateSessions(StudyPlan plan, List<Topic> topics) {
        List<StudySession> sessions = new ArrayList<>();

        LocalDate currentDate = plan.getStartDate();
        int availableHours = plan.getHoursPerDay();

        for (Topic topic : topics) {
            int remainingHours = topic.getEstimatedHours() != null && topic.getEstimatedHours() > 0
                    ? topic.getEstimatedHours()
                    : 1;

            while (remainingHours > 0) {
                if (currentDate.isAfter(plan.getEndDate())) {
                    throw new BusinessException("Not enough days to allocate all topics");
                }

                int sessionHours = Math.min(remainingHours, availableHours);

                StudySession session = StudySession.builder()
                        .studyPlan(plan)
                        .topic(topic)
                        .plannedDate(currentDate)
                        .plannedHours(sessionHours)
                        .completed(false)
                        .build();

                sessions.add(session);

                remainingHours -= sessionHours;
                availableHours -= sessionHours;

                if (availableHours == 0) {
                    currentDate = currentDate.plusDays(1);
                    availableHours = plan.getHoursPerDay();
                }
            }
        }

        return sessions;
    }

    private StudyPlanResponse toResponse(StudyPlan plan, List<StudySession> sessions) {
        return new StudyPlanResponse(
                plan.getId(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getHoursPerDay(),
                plan.getStatus(),
                plan.getCreatedAt(),
                sessions.stream().map(this::toSessionResponse).toList()
        );
    }

    private StudySessionResponse toSessionResponse(StudySession session) {
        Topic topic = session.getTopic();

        return new StudySessionResponse(
                session.getId(),
                topic.getId(),
                topic.getName(),
                topic.getSubject().getId(),
                topic.getSubject().getName(),
                session.getPlannedDate(),
                session.getPlannedHours(),
                session.getCompleted(),
                session.getCompletedAt()
        );
    }
}
