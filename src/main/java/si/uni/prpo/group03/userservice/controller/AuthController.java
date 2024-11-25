package si.uni.prpo.group03.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.uni.prpo.group03.userservice.dto.*;
import si.uni.prpo.group03.userservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserDTO user = authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        authService.confirmUser(token);
        return ResponseEntity.status(HttpStatus.OK).body("Account confirmed successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO user = authService.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody PasswordResetReqDTO passwordResetRequest) {
        authService.requestPasswordReset(passwordResetRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password reset email sent successfully!");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO passwordReset) {
        authService.resetPassword(passwordReset);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully!");
    }
}