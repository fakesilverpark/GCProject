package bssm.be.article.controller;

import bssm.be.article.dto.ArticleRequest;
import bssm.be.article.dto.ArticleResponse;
import bssm.be.article.dto.ArticleSliceResponse;
import bssm.be.article.service.ArticleService;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                  @Valid @RequestBody ArticleRequest request) {
        User user = userService.findById(principal.getUserId());
        return ResponseEntity.ok(articleService.create(request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> readOne(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.readOne(id));
    }

    @GetMapping
    public ResponseEntity<ArticleSliceResponse> readAll(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String mood) {
        return ResponseEntity.ok(articleService.readAll(lastId, size, mood));
    }

    @GetMapping("/mine")
    public ResponseEntity<ArticleSliceResponse> readMine(@AuthenticationPrincipal UserPrincipal principal,
                                                         @RequestParam(required = false) Long lastId,
                                                         @RequestParam(defaultValue = "10") int size) {
        User user = userService.findById(principal.getUserId());
        return ResponseEntity.ok(articleService.readMine(lastId, size, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserPrincipal principal,
                                                  @Valid @RequestBody ArticleRequest request) {
        User user = userService.findById(principal.getUserId());
        return ResponseEntity.ok(articleService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.findById(principal.getUserId());
        articleService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
