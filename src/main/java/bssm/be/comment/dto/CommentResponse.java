package bssm.be.comment.dto;

import bssm.be.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final Long id;
    private final Long articleId;
    private final String content;
    private final boolean deleted;
    private final AuthorProfile author;
    private final String path;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.articleId = comment.getArticle().getId();
        this.content = comment.getContent();
        this.deleted = comment.isDeleted();
        this.author = new AuthorProfile(comment.getAuthor().getId(), comment.getAuthor().getDisplayName());
        this.path = comment.getPath();
        this.createdAt = comment.getCreatedAt();
    }

    public record AuthorProfile(Long id, String displayName) {
    }
}
