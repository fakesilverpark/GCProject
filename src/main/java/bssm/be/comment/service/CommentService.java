package bssm.be.comment.service;

import bssm.be.article.domain.Article;
import bssm.be.article.repository.ArticleRepository;
import bssm.be.comment.domain.Comment;
import bssm.be.comment.dto.CommentCreateRequest;
import bssm.be.comment.dto.CommentResponse;
import bssm.be.comment.dto.CommentUpdateRequest;
import bssm.be.comment.repository.CommentRepository;
import bssm.be.common.exception.ForbiddenException;
import bssm.be.common.exception.NotFoundException;
import bssm.be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentResponse create(Long articleId, CommentCreateRequest request, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        String parentPath = null;
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findByIdAndArticleId(request.getParentId(), articleId)
                    .orElseThrow(() -> new NotFoundException("상위 댓글이 존재하지 않습니다."));
            parentPath = parent.getPath();
        }

        Comment comment = new Comment(article, user, request.getContent());
        commentRepository.save(comment);
        comment.assignPath(parentPath);
        commentRepository.save(comment);
        return new CommentResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> readAll(Long articleId) {
        return commentRepository.findByArticleIdOrderByPathAsc(articleId).stream()
                .map(CommentResponse::new)
                .toList();
    }

    public CommentResponse update(Long commentId, CommentUpdateRequest request, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (!comment.isOwner(user)) {
            throw new ForbiddenException("작성자만 수정할 수 있습니다.");
        }
        comment.updateContent(request.getContent());
        return new CommentResponse(comment);
    }

    public void delete(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (!comment.isOwner(user)) {
            throw new ForbiddenException("작성자만 삭제할 수 있습니다.");
        }
        comment.markDeleted();
        purgeIfOrphan(comment);
    }

    private void purgeIfOrphan(Comment comment) {
        boolean hasChild = commentRepository.existsChild(comment.getPath(), comment.getId());
        if (hasChild) {
            return;
        }
        commentRepository.delete(comment);
        String parentPath = comment.parentPath();
        if (parentPath != null) {
            commentRepository.findByPath(parentPath).ifPresent(this::purgeIfOrphan);
        }
    }
}
