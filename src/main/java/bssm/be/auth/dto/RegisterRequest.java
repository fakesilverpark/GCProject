package bssm.be.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank
    private String displayName;

    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    @NotBlank
    private String password;

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
