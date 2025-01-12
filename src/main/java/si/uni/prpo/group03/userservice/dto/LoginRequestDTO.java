package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for user login request")
public class LoginRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "Password for the user's account", example = "password123", hidden = true)
    private String password;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
