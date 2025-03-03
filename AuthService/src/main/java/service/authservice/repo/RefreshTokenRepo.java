package service.authservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import service.authservice.entity.RefreshToken;
import service.authservice.entity.User;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByToken(String token);
}
