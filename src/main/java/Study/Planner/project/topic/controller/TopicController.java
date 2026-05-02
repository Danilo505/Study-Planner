package Study.Planner.project.topic.controller;

import Study.Planner.project.topic.dto.*;
import Study.Planner.project.topic.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicResponse> create(
            Authentication auth,
            @RequestBody @Valid CreateTopicRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAll(Authentication auth) {
        return ResponseEntity.ok(topicService.findAll(auth.getName()));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TopicResponse>> findBySubject(
            Authentication auth,
            @PathVariable UUID subjectId
    ) {
        return ResponseEntity.ok(topicService.findBySubject(auth.getName(), subjectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> findById(
            Authentication auth,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(topicService.findById(auth.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> update(
            Authentication auth,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateTopicRequest request
    ) {
        return ResponseEntity.ok(topicService.update(auth.getName(), id, request));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> toggleComplete(
            Authentication auth,
            @PathVariable UUID id
    ) {
        topicService.toggleComplete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication auth,
            @PathVariable UUID id
    ) {
        topicService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
