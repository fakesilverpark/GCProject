package bssm.be.article.domain;

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
import org.springframework.util.StringUtils;

@Entity
@Table(name = "articles")
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 32)
    private String mood;

    protected Article() {
    }

    public Article(String title, String content, String mood, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.mood = StringUtils.hasText(mood) ? mood : "lost";
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public String getMood() {
        return mood;
    }

    public void update(String title, String content, String mood) {
        this.title = title;
        this.content = content;
        this.mood = StringUtils.hasText(mood) ? mood : this.mood;
    }
}
