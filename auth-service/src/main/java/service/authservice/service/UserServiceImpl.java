package service.authservice.service;

import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import service.authservice.entity.DTO.BasicDetailDTO;
import service.authservice.entity.RefreshToken;
import service.authservice.entity.User;
import org.springframework.stereotype.Service;
import service.authservice.repo.RefreshTokenRepo;
import service.authservice.repo.UserRepo;
import service.authservice.service.itf.JWTService;
import service.authservice.service.itf.RefreshTokenService;
import service.authservice.service.itf.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public UserServiceImpl(AuthenticationManager authManager, JWTService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo, UserRepo userRepo) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Map<String, String> verify(User user) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        User activeUser = userRepo.findByUsername(user.getUsername()).get();
        if (!auth.isAuthenticated()) {
            return Map.of();
        }
        String newRefreshToken = refreshTokenService.generateRefreshToken(user.getId());
        String newJWT = jwtService.generateToken(user.getUsername());

        refreshTokenRepo.save(RefreshToken.builder()
                .token(newRefreshToken)
                .user(activeUser)
                .expiration(Instant.now().plus(Duration.ofSeconds(30)))
                .build());
        return Map.of("jwt", newJWT, "refresh", newRefreshToken);
    }

    @Override
    public Optional<BasicDetailDTO> getBasicDetails(String username) {
        return userRepo.findByUsernameWithRoles(username)
                .map(u -> new BasicDetailDTO(u.getUsername(),
                        u.getEmail(),
                        u.getRoles()));
    }
}
