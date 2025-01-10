package si.uni.prpo.group03.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.uni.prpo.group03.userservice.dto.*;
import si.uni.prpo.group03.userservice.service.AuthService;

@Tag(name = "Authentication", description = "Controller for user authentication and account management")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Validate JWT token", description = "Validates a JWT token and returns the associated user ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token is valid, returns user ID", 
                     content = @Content(schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "401", description = "Token is invalid", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        String userId = authService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    @Operation(summary = "Register new user", description = "Registers a new user with provided details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully", 
                     content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid registration data", content = @Content),
        @ApiResponse(responseCode = "409", description = "User already exists", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        // If a UserAlreadyExistsException is thrown, GlobalExceptionHandler
        // will map it to a 409 conflict response.
        UserDTO user = authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Confirm user account", description = "Confirms the user account using a confirmation token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account confirmed successfully", 
                     content = @Content(schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found (or token invalid)", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        authService.confirmUser(token);
        return ResponseEntity.status(HttpStatus.OK).body("Account confirmed successfully!");
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token along with user details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged in successfully", 
                     content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials or unconfirmed account", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO user = authService.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Request password reset", description = "Initiates a password reset request by sending a reset email.")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Password reset email sent", 
                     content = @Content(schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/password-reset-request")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody PasswordResetReqDTO passwordResetRequest) {
        authService.requestPasswordReset(passwordResetRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password reset email sent successfully!");
    }

    @Operation(summary = "Reset password", description = "Resets the user's password using provided token and new password.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successfully", 
                     content = @Content(schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Invalid reset data or token", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found or token invalid", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO passwordReset) {
        authService.resetPassword(passwordReset);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully!");
    }
}
