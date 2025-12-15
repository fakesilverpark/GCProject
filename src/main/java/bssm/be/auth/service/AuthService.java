package bssm.be.auth.service;

import bssm.be.auth.dto.AuthResponse;
import bssm.be.auth.dto.LoginRequest;
import bssm.be.auth.dto.RegisterRequest;
import bssm.be.common.security.JwtTokenProvider;
import bssm.be.user.domain.User;
import bssm.be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = userService.register(request.getDisplayName(), request.getPassword());
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userService.authenticate(request.getDisplayName(), request.getPassword());
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, user);
    }
}
