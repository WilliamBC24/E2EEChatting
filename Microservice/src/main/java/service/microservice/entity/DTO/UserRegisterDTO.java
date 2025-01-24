package service.microservice.entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import service.microservice.utils.PasswordMatch;
import service.microservice.utils.ValidEmail;

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
    @ValidEmail
    private String email;
}
