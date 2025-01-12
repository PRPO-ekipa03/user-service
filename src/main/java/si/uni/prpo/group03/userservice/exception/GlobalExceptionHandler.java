package si.uni.prpo.group03.userservice.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MalformedJwtException.class)
    @ApiResponse(responseCode = "401", description = "Invalid token format")
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.TEXT_PLAIN).body("Invalid token format");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ApiResponse(responseCode = "409", description = "User already exists")
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ApiResponse(responseCode = "401", description = "Invalid token")
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Unexpected internal server error")
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
