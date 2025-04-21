package service.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.authservice.entity.User;
import service.authservice.entity.enums.Role;
import service.authservice.repo.UserRepo;
import service.authservice.entity.dto.RegisterDTO;
import service.authservice.service.itf.RegisterService;

import java.util.Optional;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public RegisterServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public void register(RegisterDTO accountDTO) {
        Optional<User> existingUser = userRepo.findByUsernameOrEmail(accountDTO.getUsername(), accountDTO.getEmail());
        if (existingUser.isPresent()) {
            if (existingUser.get().getUsername().equals(accountDTO.getUsername())) {
                throw new IllegalArgumentException("Username is already in use");
            }
            if (existingUser.get().getEmail().equals(accountDTO.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
        }
        String encodedPassword = passwordEncoder.encode(accountDTO.getPassword());
        User newUser = User.builder()
                .username(accountDTO.getUsername())
                .password(encodedPassword)
                .email(accountDTO.getEmail())
                .roles(Set.of(Role.USER))
                .build();
        userRepo.save(newUser);
    }
}
