package bssm.be.article.repository;

import bssm.be.article.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Slice<Article> findAllByOrderByIdDesc(Pageable pageable);

    Slice<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    Slice<Article> findByStatusOrderByIdDesc(String status, Pageable pageable);

    Slice<Article> findByStatusAndIdLessThanOrderByIdDesc(String status, Long id, Pageable pageable);
}