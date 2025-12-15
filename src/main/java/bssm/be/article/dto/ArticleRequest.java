package bssm.be.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ArticleRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String status;
}
