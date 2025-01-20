package service.microservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import service.microservice.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
