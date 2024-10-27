package si.uni.prpo.group03.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import si.uni.prpo.group03.userservice.dto.LoginRequestDTO;
import si.uni.prpo.group03.userservice.dto.RegisterRequestDTO;
import si.uni.prpo.group03.userservice.dto.UserDTO;
import si.uni.prpo.group03.userservice.dto.UserUpdateDTO;
import si.uni.prpo.group03.userservice.exception.InvalidCredentialsException;
import si.uni.prpo.group03.userservice.exception.UserAlreadyExistsException;
import si.uni.prpo.group03.userservice.exception.UserNotFoundException;
import si.uni.prpo.group03.userservice.model.User;
import si.uni.prpo.group03.userservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        user = userRepository.save(user);

        return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public UserDTO loginUser(LoginRequestDTO loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
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
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
