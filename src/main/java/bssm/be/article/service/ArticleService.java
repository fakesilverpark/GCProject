package bssm.be.article.service;

import bssm.be.article.domain.Article;
import bssm.be.article.dto.ArticleRequest;
import bssm.be.article.dto.ArticleResponse;
import bssm.be.article.dto.ArticleSliceResponse;
import bssm.be.article.repository.ArticleRepository;
import bssm.be.comment.repository.CommentRepository;
import bssm.be.common.exception.ForbiddenException;
import bssm.be.common.exception.NotFoundException;
import bssm.be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private static final Set<String> ALLOWED_STATUS = new HashSet<>(List.of("lost", "found", "resolved"));

    public ArticleResponse create(ArticleRequest request, User author) {
        Article article = new Article(request.getTitle(), request.getContent(), normalizeStatus(request.getStatus()), author);
        return new ArticleResponse(articleRepository.save(article));
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "lost";
        }
        String key = status.trim().toLowerCase();
        return ALLOWED_STATUS.contains(key) ? key : "lost";
    }

    @Transactional(readOnly = true)
    public ArticleResponse readOne(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        return new ArticleResponse(article);
    }

    @Transactional(readOnly = true)
    public ArticleSliceResponse readAll(Long lastId, int size, String mood) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        String normalizedStatus = normalizeStatus(mood);
        Slice<Article> slice;
        if (StringUtils.hasText(normalizedStatus)) {
            slice = (lastId == null)
                    ? articleRepository.findByStatusOrderByIdDesc(normalizedStatus, pageable)
                    : articleRepository.findByStatusAndIdLessThanOrderByIdDesc(normalizedStatus, lastId, pageable);
        } else {
            slice = (lastId == null)
                    ? articleRepository.findAllByOrderByIdDesc(pageable)
                    : articleRepository.findByIdLessThanOrderByIdDesc(lastId, pageable);
        }
        return getArticleSliceResponse(slice);
    }

    @Transactional(readOnly = true)
    public ArticleSliceResponse readMine(Long lastId, int size, User user) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Article> slice = (lastId == null)
                ? articleRepository.findByAuthorIdOrderByIdDesc(user.getId(), pageable)
                : articleRepository.findByAuthorIdAndIdLessThanOrderByIdDesc(user.getId(), lastId, pageable);
        return getArticleSliceResponse(slice);
    }

    private ArticleSliceResponse getArticleSliceResponse(Slice<Article> slice) {
        List<ArticleResponse> items = slice.getContent().stream()
                .map(ArticleResponse::new)
                .toList();
        Long nextCursor = slice.hasNext() && !items.isEmpty()
                ? items.get(items.size() - 1).getId()
                : null;
        return new ArticleSliceResponse(items, nextCursor);
    }

    public ArticleResponse update(Long id, ArticleRequest request, User user) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if (!article.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("작성자만 수정할 수 있습니다.");
        }
        article.update(request.getTitle(), request.getContent(), normalizeStatus(request.getStatus()));
        return new ArticleResponse(article);
    }

    public void delete(Long id, User user) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if (!article.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.deleteByArticleId(id);
        articleRepository.delete(article);
    }
}
