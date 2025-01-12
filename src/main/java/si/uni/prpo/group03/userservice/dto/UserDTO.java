package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing user details")
public class UserDTO {

    @Schema(description = "Unique username of the user", example = "johndoe")
    private final String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private final String email;

    @Schema(description = "First name of the user", example = "John")
    private final String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private final String lastName;

    public UserDTO(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
