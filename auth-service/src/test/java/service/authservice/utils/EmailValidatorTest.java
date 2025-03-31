package service.authservice.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {
    EmailValidator emailValidator;
    Pattern emailPattern;
    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid() {
        assertTrue(emailValidator.isValid("email@email.com", null));
    }

    @Test
    void isInvalid() {
        assertFalse(emailValidator.isValid("email", null));
    }
}
