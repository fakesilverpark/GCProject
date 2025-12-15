package bssm.be.comment.controller;

import bssm.be.comment.dto.CommentCreateRequest;
import bssm.be.comment.dto.CommentResponse;
import bssm.be.comment.service.CommentService;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
