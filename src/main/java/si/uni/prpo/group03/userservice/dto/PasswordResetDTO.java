package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for resetting a user's password")
public class PasswordResetDTO {

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "New password for the user", example = "newpassword123", hidden = true)
    private String newPassword;

    @NotBlank(message = "Token is required")
    @Schema(description = "Token provided for password reset verification", example = "abcd1234efgh5678")
    private String token;

    // Getters and Setters
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
