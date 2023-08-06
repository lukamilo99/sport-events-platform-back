package sport.app.sport_connecting_people.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sport.app.sport_connecting_people.util.PasswordValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface PasswordCheck {

    String message() default "Wrong password format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
