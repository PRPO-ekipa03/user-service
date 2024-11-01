package si.uni.prpo.group03.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.uni.prpo.group03.userservice.model.User;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByConfirmationToken(String confirmationToken);
    Optional<User> findByResetToken(String resetToken);

    void deleteByConfirmationExpiresAtBefore(Instant now);

    @Modifying
    @Query("UPDATE User u SET u.resetToken = null, u.resetExpiresAt = null WHERE u.resetExpiresAt < :now")
    void clearExpiredResetTokens(Instant now);
}
