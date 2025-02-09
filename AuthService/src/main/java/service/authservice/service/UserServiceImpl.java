package service.authservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import service.authservice.entity.User;
import org.springframework.stereotype.Service;
import service.authservice.service.itf.JWTService;
import service.authservice.service.itf.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public UserServiceImpl(AuthenticationManager authManager, JWTService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public String verify(User user) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (auth.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Verification failed";
        }
    }
}
