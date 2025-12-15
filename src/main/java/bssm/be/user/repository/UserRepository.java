package bssm.be.user.repository;

import bssm.be.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDisplayName(String displayName);
    boolean existsByEmail(String email);
    boolean existsByDisplayName(String displayName);
}
