package service.authservice.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import service.authservice.entity.DTO.UserRegisterDTO;

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserRegisterDTO user = (UserRegisterDTO) obj;
        return user.getPassword().equals(user.getPasswordMatch());
    }
}
