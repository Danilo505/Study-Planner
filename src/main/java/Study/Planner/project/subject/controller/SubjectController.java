package Study.Planner.project.subject.controller;

import Study.Planner.project.subject.dto.CreateSubjectRequest;
import Study.Planner.project.subject.dto.SubjectResponse;
import Study.Planner.project.subject.dto.UpdateSubjectRequest;
import Study.Planner.project.subject.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponse> create(
            Authentication authentication,
            @RequestBody @Valid CreateSubjectRequest request
    ) {
        SubjectResponse response = subjectService.create(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> findAll(Authentication authentication) {
        return ResponseEntity.ok(subjectService.findAll(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> findById(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(subjectService.findById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> update(
            Authentication authentication,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateSubjectRequest request
    ) {
        return ResponseEntity.ok(subjectService.update(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        subjectService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
