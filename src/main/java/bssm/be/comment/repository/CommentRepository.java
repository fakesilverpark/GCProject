package bssm.be.comment.repository;

import bssm.be.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteByArticleId(Long articleId);

    Optional<Comment> findByIdAndArticleId(Long id, Long articleId);

    List<Comment> findByArticleIdOrderByPathAsc(Long articleId);

    Optional<Comment> findByPath(String path);

    @Query("select case when count(c)>0 then true else false end from Comment c where c.path like concat(:pathPrefix, '/%') and c.id <> :selfId")
    boolean existsChild(@Param("pathPrefix") String pathPrefix, @Param("selfId") Long selfId);
}
