package bssm.be.comment.controller;

import bssm.be.comment.dto.CommentCreateRequest;
import bssm.be.comment.dto.CommentResponse;
import bssm.be.comment.dto.CommentUpdateRequest;
import bssm.be.comment.service.CommentService;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long articleId,
                                                  @Valid @RequestBody CommentCreateRequest request) {
        User user = userService.findById(principal.getUserId());
        return ResponseEntity.ok(commentService.create(articleId, request, user));
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponse>> readAll(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.readAll(articleId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long commentId,
                                                  @Valid @RequestBody CommentUpdateRequest request) {
        User user = userService.findById(principal.getUserId());
        return ResponseEntity.ok(commentService.update(commentId, request, user));
    }
}
