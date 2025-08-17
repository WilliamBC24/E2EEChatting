package service.authservice.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import service.authservice.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
//    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
//    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}

