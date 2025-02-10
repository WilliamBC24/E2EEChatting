package service.authservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import service.authservice.entity.Enum.Role;
import service.authservice.entity.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
