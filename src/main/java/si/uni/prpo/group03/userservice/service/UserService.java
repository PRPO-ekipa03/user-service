package si.uni.prpo.group03.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import si.uni.prpo.group03.userservice.dto.*;
import si.uni.prpo.group03.userservice.exception.InvalidCredentialsException;
import si.uni.prpo.group03.userservice.exception.UserAlreadyExistsException;
import si.uni.prpo.group03.userservice.exception.UserNotFoundException;
import si.uni.prpo.group03.userservice.model.User;
import si.uni.prpo.group03.userservice.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${notification.service.url}")
    private String notificationServiceURL;

    @Transactional
    public UserDTO registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setConfirmed(false);
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setConfirmationExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
        user = userRepository.save(user);

        String confirmationLink = "http://localhost:8080/api/users/confirm?token=" + user.getConfirmationToken();
        NotificationDTO notification = new NotificationDTO();
        notification.setEmail(user.getEmail());
        notification.setSubject("Confirm your account");
        notification.setMessage("Click the following link to confirm your account: " + confirmationLink + "\n" +
                "This link will expire in 24 hours");

        sendNotification(notification, "/api/notifications/confirmation");

        return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public void confirmUser(String confirmationToken) {
        Optional<User> optionalUser = userRepository.findByConfirmationToken(confirmationToken);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setConfirmed(true);
            user.setConfirmationToken(null);
            user.setConfirmationExpiresAt(null);
            userRepository.save(user);
            return;
        }
        throw new UserNotFoundException("User not found or token invalid");
    }

    public UserDTO loginUser(LoginRequestDTO loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(!user.isConfirmed()) {
                throw new InvalidCredentialsException("Account not confirmed");
            }

            if(isPasswordValid(loginRequest.getPassword(), user.getPassword())) {
                return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
            } else {
                throw new InvalidCredentialsException("Invalid password");
            }
        }
        throw new UserNotFoundException("User with that email not found");
    }

    @Transactional
    public void requestPasswordReset(PasswordResetReqDTO passwordResetRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(passwordResetRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            user.setResetExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
            userRepository.save(user);

            String resetLink = "http://localhost:8080/api/users/password-reset?token=" + user.getResetToken();
            NotificationDTO notification = new NotificationDTO();
            notification.setEmail(user.getEmail());
            notification.setSubject("Reset your password");
            notification.setMessage("Click the following link to reset your password: " + resetLink + "\n" +
                    "This link will expire in 24 hours");

            sendNotification(notification, "/api/notifications/password-reset");
            return;
        }
        throw new UserNotFoundException("User with that email not found");
    }

    public void resetPassword(PasswordResetDTO passwordReset) {
        Optional<User> optionalUser = userRepository.findByResetToken(passwordReset.getToken());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(encodePassword(passwordReset.getNewPassword()));
            user.setResetToken(null);
            user.setResetExpiresAt(null);
            userRepository.save(user);
            return;
        }
        throw new UserNotFoundException("User not found or token invalid");
    }

    public UserDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
        }
        throw new UserNotFoundException("User with that id not found");
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userUpdateRequest.getFirstName());
            user.setLastName(userUpdateRequest.getLastName());
            user = userRepository.save(user);

            return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
        }
        throw new UserNotFoundException("User with that id not found");
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return;
        }
        throw new UserNotFoundException("User with that id not found");
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    private void sendNotification(NotificationDTO notification, String endpoint) {
        HttpEntity<NotificationDTO> req = new HttpEntity<>(notification);
        // Spring automatically throws error for 4xx or 5xx response status code
        restTemplate.postForEntity(notificationServiceURL + endpoint, req, String.class);
    }
}
