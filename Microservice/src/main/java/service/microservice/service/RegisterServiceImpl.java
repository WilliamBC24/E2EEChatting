package service.microservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import service.microservice.entity.User;
import service.microservice.entity.Enum.Role;
import service.microservice.repo.UserRepo;
import service.microservice.entity.DTO.UserRegisterDTO;
import service.microservice.service.itf.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public RegisterServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public void register(UserRegisterDTO accountDTO, Errors errors) {
        if (userRepo.existsByUsername(accountDTO.getUsername())) {
            errors.rejectValue("username", "username.invalid", "Username already exist");
        }
        if (userRepo.existsByEmail(accountDTO.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Email already in use");
        }
        if (!errors.hasErrors()) {
            String encodedPassword = passwordEncoder.encode(accountDTO.getPassword());
            User newUser = User.builder()
                    .username(accountDTO.getUsername())
                    .password(encodedPassword)
                    .email(accountDTO.getEmail())
                    .role(Role.USER)
                    .build();
            userRepo.save(newUser);
        }
    }
}
