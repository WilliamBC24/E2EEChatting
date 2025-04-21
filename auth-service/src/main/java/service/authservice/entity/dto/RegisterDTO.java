package service.authservice.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import service.authservice.entity.itf.PasswordValidatable;
import service.authservice.utils.PasswordMatch;

@Data
@PasswordMatch
@AllArgsConstructor
public class RegisterDTO implements PasswordValidatable {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    private String passwordMatch;

    @NotBlank
    @Email
    private String email;
}
