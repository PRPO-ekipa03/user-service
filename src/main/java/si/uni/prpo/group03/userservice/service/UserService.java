package si.uni.prpo.group03.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import si.uni.prpo.group03.userservice.dto.*;
import si.uni.prpo.group03.userservice.exception.InvalidCredentialsException;
import si.uni.prpo.group03.userservice.exception.UserAlreadyExistsException;
import si.uni.prpo.group03.userservice.exception.UserNotFoundException;
import si.uni.prpo.group03.userservice.model.User;
import si.uni.prpo.group03.userservice.repository.UserRepository;

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
        user = userRepository.save(user);

        String confirmationLink = "http://localhost:8080/api/users/confirm?token=" + user.getConfirmationToken();
        NotificationDTO notification = new NotificationDTO();
        notification.setEmail(user.getEmail());
        notification.setSubject("Confirm your account");
        notification.setMessage("Click the following link to confirm your account: " + confirmationLink);

        String notificationServiceURL = "http://localhost:8081/api/notifications/confirmation";

        HttpEntity<NotificationDTO> req = new HttpEntity<>(notification);

        // Spring automatically throws error for 4xx or 5xx response status code
        ResponseEntity<String> response = restTemplate.postForEntity(notificationServiceURL, req, String.class);

        return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public void confirmUser(String confirmationToken) {
        Optional<User> optionalUser = userRepository.findByConfirmationToken(confirmationToken);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setConfirmed(true);
            user.setConfirmationToken(null);
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
}
