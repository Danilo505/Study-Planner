package Study.Planner.project.exam.controller;

import Study.Planner.project.exam.dto.CreateExamRequest;
import Study.Planner.project.exam.dto.ExamResponse;
import Study.Planner.project.exam.dto.UpdateExamRequest;
import Study.Planner.project.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class   ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponse> create(
            Authentication authentication,
            @RequestBody @Valid CreateExamRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examService.create(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<ExamResponse>> findAll(Authentication authentication) {
        return ResponseEntity.ok(examService.findAll(authentication.getName()));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ExamResponse>> findUpcoming(Authentication authentication) {
        return ResponseEntity.ok(examService.findUpcoming(authentication.getName()));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<ExamResponse>> findBySubject(
            Authentication authentication,
            @PathVariable UUID subjectId
    ) {
        return ResponseEntity.ok(examService.findBySubject(authentication.getName(), subjectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> findById(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(examService.findById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> update(
            Authentication authentication,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateExamRequest request
    ) {
        return ResponseEntity.ok(examService.update(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        examService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
