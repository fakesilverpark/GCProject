package bssm.be.user.service;

import bssm.be.common.exception.BadRequestException;
import bssm.be.common.exception.NotFoundException;
import bssm.be.common.security.UserPrincipal;
import bssm.be.user.domain.User;
import bssm.be.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String displayName, String rawPassword) {
        String normalized = normalize(displayName);
        if (userRepository.existsByDisplayName(normalized)) {
            throw new BadRequestException("이미 사용 중인 아이디입니다.");
        }
        String email = generateEmail(normalized);
        User user = new User(email, normalized, passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    public User authenticate(String displayName, String password) {
        String normalized = normalize(displayName);
        User user = userRepository.findByDisplayName(normalized)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = normalize(username);
        User user = userRepository.findByDisplayName(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserPrincipal(user);
    }

    private String normalize(String raw) {
        if (raw == null) return "";
        return raw.trim().toLowerCase();
    }

    private String generateEmail(String displayName) {
        String base = displayName.isBlank() ? "user" : displayName.replaceAll("[^a-z0-9]", "");
        if (base.isBlank()) base = "user";
        String candidate = base + "@local.dev";
        int counter = 1;
        while (userRepository.existsByEmail(candidate)) {
            candidate = base + counter + "@local.dev";
            counter++;
        }
        return candidate;
    }
}
