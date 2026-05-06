package Study.Planner.project.studyplan.controller;

import Study.Planner.project.studyplan.dto.GenerateStudyPlanRequest;
import Study.Planner.project.studyplan.dto.StudyPlanResponse;
import Study.Planner.project.studyplan.dto.StudySessionResponse;
import Study.Planner.project.studyplan.service.StudyPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/study-plans")
@RequiredArgsConstructor
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    @PostMapping("/generate")
    public ResponseEntity<StudyPlanResponse> generate(
            Authentication authentication,
            @RequestBody @Valid GenerateStudyPlanRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studyPlanService.generate(authentication.getName(), request));
    }

    @GetMapping("/current")
    public ResponseEntity<StudyPlanResponse> getCurrent(Authentication authentication) {
        return ResponseEntity.ok(studyPlanService.getCurrent(authentication.getName()));
    }

    @GetMapping("/sessions/today")
    public ResponseEntity<List<StudySessionResponse>> getTodaySessions(Authentication authentication) {
        return ResponseEntity.ok(studyPlanService.getTodaySessions(authentication.getName()));
    }

    @PatchMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            Authentication authentication,
            @PathVariable UUID sessionId
    ) {
        studyPlanService.completeSession(authentication.getName(), sessionId);
        return ResponseEntity.noContent().build();
    }
}
