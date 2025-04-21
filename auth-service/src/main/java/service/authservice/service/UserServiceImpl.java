package service.authservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import service.authservice.entity.RefreshToken;
import service.authservice.entity.User;
import org.springframework.stereotype.Service;
import service.authservice.entity.dto.LoginDTO;
import service.authservice.repo.RefreshTokenRepo;
import service.authservice.service.itf.JWTService;
import service.authservice.service.itf.RefreshTokenService;
import service.authservice.service.itf.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;

    public UserServiceImpl(AuthenticationManager authManager, JWTService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Override
    public Map<String, String> verify(LoginDTO loginDTO) {
        //User auth with Spring Security
        //This will user UserDetailsService, and as it returns User, you can just pull that out
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        if (!auth.isAuthenticated()) {
            return Map.of();
        }
        User user = (User) auth.getPrincipal();

        String newRefreshToken = refreshTokenService.generateRefreshToken();
        String newJWT = jwtService.generateToken(loginDTO.getUsername());

        refreshTokenRepo.save(RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiration(Instant.now().plus(Duration.ofSeconds(30)))
                .build());

        return Map.of("jwt", newJWT, "refresh", newRefreshToken);
    }
}
