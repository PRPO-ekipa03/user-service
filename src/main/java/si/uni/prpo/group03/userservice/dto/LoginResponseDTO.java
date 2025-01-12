package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing the response for a successful login")
public class LoginResponseDTO {

    @Schema(description = "JWT token issued upon successful login", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private final String token;

    @Schema(description = "Username of the authenticated user", example = "johndoe")
    private final String username;

    @Schema(description = "Email address of the authenticated user", example = "john.doe@example.com")
    private final String email;

    public LoginResponseDTO(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
