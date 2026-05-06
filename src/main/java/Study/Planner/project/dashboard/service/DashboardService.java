package Study.Planner.project.dashboard.service;

import Study.Planner.project.dashboard.dto.DashboardResponse;
import Study.Planner.project.dashboard.dto.NextExamResponse;
import Study.Planner.project.exam.entity.Exam;
import Study.Planner.project.exam.repository.ExamRepository;
import Study.Planner.project.studyplan.entity.StudySession;
import Study.Planner.project.studyplan.repository.StudySessionRepository;
import Study.Planner.project.subject.repository.SubjectRepository;
import Study.Planner.project.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final ExamRepository examRepository;
    private final StudySessionRepository studySessionRepository;

    public DashboardResponse getDashboard(String email) {

        Long totalSubjects = subjectRepository.countByUserEmail(email);

        Long totalTopics = topicRepository.countBySubjectUserEmail(email);

        Long completedTopics =
                topicRepository.countBySubjectUserEmailAndCompletedTrue(email);

        Long pendingTopics =
                topicRepository.countBySubjectUserEmailAndCompletedFalse(email);

        Long totalExams =
                examRepository.countByUserEmail(email);

        Long upcomingExams =
                examRepository.countByUserEmailAndExamDateGreaterThanEqual(
                        email,
                        LocalDate.now()
                );

        Long completedSessions =
                studySessionRepository.countByStudyPlanUserEmailAndCompletedTrue(email);

        Long pendingSessions =
                studySessionRepository.countByStudyPlanUserEmailAndCompletedFalse(email);

        List<StudySession> sessions =
                studySessionRepository.findAllByStudyPlanUserEmail(email);

        Integer totalPlannedHours = sessions.stream()
                .mapToInt(StudySession::getPlannedHours)
                .sum();

        Integer completedHours = sessions.stream()
                .filter(StudySession::getCompleted)
                .mapToInt(StudySession::getPlannedHours)
                .sum();

        Double progressPercentage = totalPlannedHours > 0
                ? (completedHours * 100.0) / totalPlannedHours
                : 0.0;

        NextExamResponse nextExamResponse = examRepository
                .findFirstByUserEmailAndExamDateGreaterThanEqualOrderByExamDateAsc(
                        email,
                        LocalDate.now()
                )
                .map(this::toNextExamResponse)
                .orElse(null);

        return new DashboardResponse(
                totalSubjects,
                totalTopics,
                completedTopics,
                pendingTopics,
                totalExams,
                upcomingExams,
                completedSessions,
                pendingSessions,
                totalPlannedHours,
                completedHours,
                progressPercentage,
                nextExamResponse
        );
    }

    private NextExamResponse toNextExamResponse(Exam exam) {
        return new NextExamResponse(
                exam.getTitle(),
                exam.getSubject().getName(),
                exam.getExamDate()
        );
    }
}
