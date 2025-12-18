package bssm.be.article.dto;

import bssm.be.article.domain.Article;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final AuthorProfile author;
    private final String status;
    @JsonProperty("mood")
    private final String mood;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = new AuthorProfile(article.getAuthor().getId(), article.getAuthor().getDisplayName());
        this.status = article.getStatus();
        this.mood = article.getStatus();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
    }

    public record AuthorProfile(Long id, String displayName) {
    }
}
