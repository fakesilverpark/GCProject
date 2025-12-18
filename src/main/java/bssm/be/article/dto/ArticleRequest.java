package bssm.be.article.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ArticleRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @JsonAlias("mood")
    private String status;
}
