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
            User chris = userRepository.save(new User("chris@local.dev", "chris", encoder.encode("password1")));

            Article lost1 = articleRepository.save(new Article("지갑을 잃어버렸어요", "도서관 1층에서 검정색 반지갑을 분실했습니다. 학생증/카드가 들어있어요.", "lost", alice));
            Article lost2 = articleRepository.save(new Article("에어팟 케이스 분실", "운동장 벤치 근처에서 흰색 케이스를 잃어버렸습니다.", "lost", bob));
            Article lost3 = articleRepository.save(new Article("체육복 상의 분실", "체육관 라커룸에서 남색 체육복 상의를 분실했습니다.", "lost", chris));

            Article found1 = articleRepository.save(new Article("노트북 파우치 습득", "공학관 2층 테이블에서 회색 파우치를 주웠습니다. 관리자실에 맡겨두었습니다.", "found", bob));
            Article found2 = articleRepository.save(new Article("학생증 습득", "식당 입구에서 학생증을 주웠습니다. 성함 확인 부탁드려요.", "found", alice));
            Article found3 = articleRepository.save(new Article("우산 습득", "기숙사 로비에서 검정 우산을 보관 중입니다.", "found", chris));

            Article resolved1 = articleRepository.save(new Article("보조배터리 찾았어요", "학생회관에서 분실했는데 주워주신 분께 받았습니다.", "resolved", alice));
            Article resolved2 = articleRepository.save(new Article("모자 해결 완료", "체육관 앞에서 잃어버린 모자를 되찾았습니다.", "resolved", bob));
            Article resolved3 = articleRepository.save(new Article("텀블러 해결", "도서관 카페에 두고 온 텀블러를 찾아왔습니다.", "resolved", chris));

            Comment c1 = commentRepository.save(new Comment(lost1, bob, "비슷한 지갑을 1층 게시판 옆에서 봤어요."));
            c1.assignPath(null);
            commentRepository.save(c1);

            Comment c2 = commentRepository.save(new Comment(lost1, alice, "혹시 사진 공유 가능할까요?"));
            c2.assignPath(c1.getPath());
            commentRepository.save(c2);

            Comment c3 = commentRepository.save(new Comment(lost1, bob, "지갑 앞면에 스티커가 붙어 있었어요."));
            c3.assignPath(c2.getPath());
            commentRepository.save(c3);

            Comment c4 = commentRepository.save(new Comment(lost2, chris, "운동장 관리실에 문의해보세요."));
            c4.assignPath(null);
            commentRepository.save(c4);

            Comment c5 = commentRepository.save(new Comment(found1, alice, "제 소유 같아요! 연락처 남깁니다."));
            c5.assignPath(null);
            commentRepository.save(c5);

            Comment c6 = commentRepository.save(new Comment(found1, bob, "확인 후 연락드릴게요."));
            c6.assignPath(c5.getPath());
            commentRepository.save(c6);

            Comment c7 = commentRepository.save(new Comment(found2, chris, "성함 알려주시면 확인해볼게요."));
            c7.assignPath(null);
            commentRepository.save(c7);

            Comment c8 = commentRepository.save(new Comment(resolved1, bob, "다행이에요!"));
            c8.assignPath(null);
            commentRepository.save(c8);

            Comment c9 = commentRepository.save(new Comment(resolved2, alice, "찾으셔서 다행입니다."));
            c9.assignPath(null);
            commentRepository.save(c9);

            Comment c10 = commentRepository.save(new Comment(resolved3, bob, "카페 직원분께 감사드려요."));
            c10.assignPath(null);
            commentRepository.save(c10);
        };
    }
}
