package bssm.be.article.controller;

import bssm.be.article.dto.ArticleResponse;
import bssm.be.article.service.ArticleService;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
