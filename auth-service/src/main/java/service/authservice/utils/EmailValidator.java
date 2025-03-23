package service.authservice.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // No initialization required
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        return email!=null && EMAIL_PATTERN.matcher(email).matches();
    }
}