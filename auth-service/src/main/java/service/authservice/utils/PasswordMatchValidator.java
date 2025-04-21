package service.authservice.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import service.authservice.entity.itf.PasswordValidatable;

public class PasswordMatchValidator
    //Specifies which annotation it validates
        implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        //No initialization required
    }

    @Override
    //Context is used for custom error
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        //This assigns the value of obj to user if it is an instance
        if (obj instanceof PasswordValidatable user) {
            return user.getPassword().equals(user.getPasswordMatch());
        }
        return false;
    }
}
