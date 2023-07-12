package sport.app.sport_connecting_people.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateDto {

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @Email
    private String email;
}
