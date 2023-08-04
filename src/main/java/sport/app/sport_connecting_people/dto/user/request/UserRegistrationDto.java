package sport.app.sport_connecting_people.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sport.app.sport_connecting_people.annotation.PasswordCheck;

@PasswordCheck
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatedPassword;
    @Email
    private String email;
}
