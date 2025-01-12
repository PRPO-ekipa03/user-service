package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for requesting a password reset")
public class PasswordResetReqDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address associated with the user's account", example = "john.doe@example.com")
    private String email;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
