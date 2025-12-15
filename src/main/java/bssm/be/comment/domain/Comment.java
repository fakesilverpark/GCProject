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

import java.util.Arrays;
import java.util.List;

@Getter
@Entity
@Table(name = "comments")
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

    protected Comment() {
    }

    public Comment(Article article, User author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
        this.path = "";
    }
}
