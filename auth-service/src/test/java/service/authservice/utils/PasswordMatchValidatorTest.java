package service.authservice.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.authservice.entity.dto.RegisterDTO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordMatchValidatorTest {
    PasswordMatchValidator passwordMatchValidator;
    @BeforeEach
    void setUp() {
        passwordMatchValidator = new PasswordMatchValidator();
    }
    @Test
    void passwordMatch() {
        RegisterDTO userRegisterDTO = new RegisterDTO(
                "username", "pass", "pass", "email@email.com");
        assertTrue(passwordMatchValidator.isValid(userRegisterDTO, null));
    }

    @Test
    void passwordNotMatch() {
        RegisterDTO userRegisterDTO = new RegisterDTO(
                "username", "pass", "notpass", "email@email.com");
        assertFalse(passwordMatchValidator.isValid(userRegisterDTO, null));
    }
}
