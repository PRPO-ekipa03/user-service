package si.uni.prpo.group03.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@Schema(description = "User entity representing system users with credentials and profile information.")
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name cannot be empty")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name cannot be empty")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be empty")
    @Schema(description = "Unique username of the user", example = "johndoe")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be empty")
    @Schema(hidden = true)  // Hide password from documentation for security
    private String password;

    @Column(nullable = false)
    @Schema(description = "Indicates if the user account is confirmed", example = "true")
    private boolean confirmed;

    @Schema(hidden = true)
    private String confirmationToken;

    @Schema(hidden = true)
    private Instant confirmationExpiresAt;

    @Schema(hidden = true)
    private String resetToken;

    @Schema(hidden = true)
    private Instant resetExpiresAt;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the user was created", example = "2023-08-15T14:30:00Z")
    private Instant created;

    @Column
    @Schema(description = "Timestamp when the user was last updated", example = "2023-08-16T10:00:00Z")
    private Instant updated;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.updated = now;
        this.created = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    @Override
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public boolean isConfirmed() {
        return confirmed;
    }
 
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
 
    public String getConfirmationToken() {
        return confirmationToken;
    }
 
    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
 
    public Instant getConfirmationExpiresAt() {
        return confirmationExpiresAt;
    }
 
    public void setConfirmationExpiresAt(Instant confirmationExpiresAt) {
        this.confirmationExpiresAt = confirmationExpiresAt;
    }
 
    public String getResetToken() {
        return resetToken;
    }
 
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
 
    public Instant getResetExpiresAt() {
        return resetExpiresAt;
    }
 
    public void setResetExpiresAt(Instant resetExpiresAt) {
        this.resetExpiresAt = resetExpiresAt;
    }
 
    public Instant getCreated() {
        return created;
    }
 
    public Instant getUpdated() {
        return updated;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming all users have a single role "ROLE_USER"
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return confirmed;
    }
}
