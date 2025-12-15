package bssm.be.article.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ArticleSliceResponse {
    private final List<ArticleResponse> items;
    private final Long nextCursor;

    public ArticleSliceResponse(List<ArticleResponse> items, Long nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }
}
