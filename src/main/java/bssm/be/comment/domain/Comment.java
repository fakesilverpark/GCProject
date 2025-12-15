package bssm.be.comment.domain;

import bssm.be.article.domain.Article;
import bssm.be.common.entity.BaseTimeEntity;
import bssm.be.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    private static final int SEGMENT_LENGTH = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private boolean deleted = false;

    public Comment(Article article, User author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
        this.path = "";
    }

    public void assignPath(String parentPath) {
        String segment = String.format("%0" + SEGMENT_LENGTH + "d", id);
        this.path = (parentPath == null || parentPath.isBlank())
                ? "/" + segment
                : parentPath + "/" + segment;
    }

    public boolean isOwner(User user) {
        return author.getId().equals(user.getId());
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void markDeleted() {
        this.deleted = true;
        this.content = "(deleted)";
    }

    public String parentPath() {
        if (path == null || path.isBlank()) {
            return null;
        }
        List<String> parts = Arrays.stream(path.split("/"))
                .filter(s -> !s.isEmpty())
                .toList();
        if (parts.size() <= 1) {
            return null;
        }
        return "/" + String.join("/", parts.subList(0, parts.size() - 1));
    }
}
