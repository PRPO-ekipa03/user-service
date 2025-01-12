package si.uni.prpo.group03.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for sending notifications")
public class NotificationDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Recipient's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Message should not be empty")
    @Schema(description = "Message content of the notification", example = "Your account has been successfully created.")
    private String message;

    @NotBlank(message = "Subject should not be empty")
    @Schema(description = "Subject of the notification email", example = "Account Created Successfully")
    private String subject;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
