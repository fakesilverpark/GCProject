package bssm.be.config;

import bssm.be.article.domain.Article;
import bssm.be.article.repository.ArticleRepository;
import bssm.be.comment.domain.Comment;
import bssm.be.comment.repository.CommentRepository;
import bssm.be.user.domain.User;
import bssm.be.user.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               ArticleRepository articleRepository,
                               CommentRepository commentRepository,
                               PasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            User alice = userRepository.save(new User("alice@local.dev", "alice", encoder.encode("password1")));
            User bob = userRepository.save(new User("bob@local.dev", "bob", encoder.encode("password1")));

            Article a1 = articleRepository.save(new Article("지갑을 잃어버렸어요", "도서관 1층에서 검정색 반지갑을 분실했습니다. 학생증/카드가 들어있어요.", "lost", alice));
            Article a2 = articleRepository.save(new Article("노트북 파우치 습득", "공학관 2층 테이블에서 회색 파우치를 주웠습니다. 관리자실에 맡겨두었습니다.", "found", bob));

            Comment c1 = commentRepository.save(new Comment(a1, bob, "비슷한 지갑을 1층 게시판 옆에서 봤어요."));
            c1.assignPath(null);
            commentRepository.save(c1);

            Comment c2 = commentRepository.save(new Comment(a1, alice, "혹시 사진 공유 가능할까요?"));
            c2.assignPath(c1.getPath());
            commentRepository.save(c2);

            Comment c3 = commentRepository.save(new Comment(a2, alice, "제 소유 같아요! 연락처 남깁니다."));
            c3.assignPath(null);
            commentRepository.save(c3);

            Comment c4 = commentRepository.save(new Comment(a2, bob, "확인 후 연락드릴게요."));
            c4.assignPath(c3.getPath());
            commentRepository.save(c4);
        };
    }
}
