package si.uni.prpo.group03.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import si.uni.prpo.group03.userservice.repository.UserRepository;

import java.time.Instant;

@Service
public class CleanupService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(fixedRate = 86400000) // clean up every 24 hours
    public void cleanUpExpiredTokens() {
        Instant now = Instant.now();
        userRepository.deleteByConfirmationExpiresAtBefore(now);
        userRepository.clearExpiredResetTokens(now);
    }
}