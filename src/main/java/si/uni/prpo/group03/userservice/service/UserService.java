package si.uni.prpo.group03.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import si.uni.prpo.group03.userservice.dto.UserDTO;
import si.uni.prpo.group03.userservice.dto.UserUpdateDTO;
import si.uni.prpo.group03.userservice.exception.UserNotFoundException;
import si.uni.prpo.group03.userservice.model.User;
import si.uni.prpo.group03.userservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User loadById(String userId) throws UserNotFoundException {
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
        }
        throw new UserNotFoundException("User with that id not found");
    }

    @Transactional
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

    @Transactional
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return;
        }
        throw new UserNotFoundException("User with that id not found");
    }
}