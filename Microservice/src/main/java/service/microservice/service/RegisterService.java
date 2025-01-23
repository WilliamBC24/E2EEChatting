package service.microservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import service.microservice.entity.Account;
import service.microservice.entity.Enum.Role;
import service.microservice.repo.AccountRepo;
import service.microservice.entity.DTO.AccountRegisterDTO;

@Service
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;

    public RegisterService(PasswordEncoder passwordEncoder, AccountRepo accountRepo) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepo = accountRepo;
    }

    public void register(AccountRegisterDTO accountDTO, Errors errors) {
        if (accountRepo.existsByUsername(accountDTO.getUsername())) {
            errors.rejectValue("username", "username.invalid", "Username already exist");
        }
        if (accountRepo.existsByEmail(accountDTO.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Email already in use");
        }
        if (!errors.hasErrors()) {
            String encodedPassword = passwordEncoder.encode(accountDTO.getPassword());
            Account newAccount = Account.builder()
                    .username(accountDTO.getUsername())
                    .password(encodedPassword)
                    .email(accountDTO.getEmail())
                    .role(Role.USER)
                    .build();
            accountRepo.save(newAccount);
        }
    }
}
