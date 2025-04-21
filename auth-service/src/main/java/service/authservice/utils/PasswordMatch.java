package service.authservice.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//Applied to classes, interfaces, or enums, one think its entity types
//Can also be FIELD or PARAMETER
@Target({TYPE,ANNOTATION_TYPE})
//How long the annotation is retained
@Retention(RUNTIME)
//Links validator class to annotation
@Constraint(validatedBy = PasswordMatchValidator.class)
//Include annotation in generated document
@Documented
public @interface PasswordMatch {
    //Only thing required
    String message() default "Passwords don't match";
    //Used to categorize annotations into groups, not normally needed
    Class<?>[] groups() default {};
    //To provide additional data, not normally needed
    Class<? extends Payload>[] payload() default {};
}
