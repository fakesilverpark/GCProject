package bssm.be.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String displayName;

    @NotBlank
    private String password;
}
