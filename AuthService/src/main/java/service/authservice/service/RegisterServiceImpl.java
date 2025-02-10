package service.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.authservice.entity.User;
import service.authservice.entity.Enum.Role;
import service.authservice.repo.UserRepo;
import service.authservice.entity.DTO.UserRegisterDTO;
import service.authservice.service.itf.RegisterService;

import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public RegisterServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public void register(UserRegisterDTO accountDTO) {
        if (userRepo.existsByUsername(accountDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }
        if (userRepo.existsByEmail(accountDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        String encodedPassword = passwordEncoder.encode(accountDTO.getPassword());
        User newUser = User.builder()
                .username(accountDTO.getUsername())
                .password(encodedPassword)
                .email(accountDTO.getEmail())
                .roles(List.of(Role.USER))
                .build();
        userRepo.save(newUser);
    }
}
