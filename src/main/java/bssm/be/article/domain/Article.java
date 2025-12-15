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
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
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
    private String status;

    protected Article() {
    }

    public Article(String title, String content, String status, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = StringUtils.hasText(status) ? status : "lost";
    }

    public void update(String title, String content, String mood) {
        this.title = title;
        this.content = content;
        this.status = StringUtils.hasText(mood) ? mood : this.status;
    }
}
