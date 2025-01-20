package service.microservice.service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import service.microservice.entity.DTO.AccountRegisterDTO;
import service.microservice.utils.PasswordMatch;

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        AccountRegisterDTO user = (AccountRegisterDTO) obj;
        return user.getPassword().equals(user.getPasswordMatch());
    }
}
