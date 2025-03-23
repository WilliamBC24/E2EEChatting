package service.authservice.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import service.authservice.utils.PasswordMatch;

@Data
@PasswordMatch
public class UserRegisterDTO {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String passwordMatch;

    @NotNull
    @NotEmpty
    @Email
    private String email;
}
