package sport.app.sport_connecting_people.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sport.app.sport_connecting_people.annotation.PasswordCheck;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordCheck, UserRegistrationDto> {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?!.*\\s).{8,}$";

    @Override
    public void initialize(PasswordCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationDto user, ConstraintValidatorContext constraintValidatorContext) {
        if(!user.getPassword().equals(user.getRepeatedPassword()))
            return false;

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(user.getPassword());
        return matcher.matches();
    }
}
