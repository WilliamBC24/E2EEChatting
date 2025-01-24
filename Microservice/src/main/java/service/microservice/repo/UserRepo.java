package service.microservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import service.microservice.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
