package sport.app.sport_connecting_people.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UserLoginDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
