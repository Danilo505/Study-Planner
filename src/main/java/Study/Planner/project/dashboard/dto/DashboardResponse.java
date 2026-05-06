package Study.Planner.project.dashboard.dto;

import java.time.LocalDate;

public record DashboardResponse(

        Long totalSubjects,

        Long totalTopics,

        Long completedTopics,

        Long pendingTopics,

        Long totalExams,

        Long upcomingExams,

        Long completedSessions,

        Long pendingSessions,

        Integer totalPlannedHours,

        Integer completedHours,

        Double progressPercentage,

        NextExamResponse nextExam
) {
}
