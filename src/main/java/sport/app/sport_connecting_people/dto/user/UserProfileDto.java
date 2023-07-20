package sport.app.sport_connecting_people.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private boolean isOAuth;
}