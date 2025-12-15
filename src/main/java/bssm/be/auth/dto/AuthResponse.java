package bssm.be.auth.dto;

import bssm.be.user.domain.User;

public class AuthResponse {
    private final String token;
    private final UserProfile user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = new UserProfile(user.getId(), user.getEmail(), user.getDisplayName());
    }

    public String getToken() {
        return token;
    }

    public UserProfile getUser() {
        return user;
    }

    public record UserProfile(Long id, String email, String displayName) {
    }
}
