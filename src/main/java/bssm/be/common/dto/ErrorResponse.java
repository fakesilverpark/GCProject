package bssm.be.common.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
