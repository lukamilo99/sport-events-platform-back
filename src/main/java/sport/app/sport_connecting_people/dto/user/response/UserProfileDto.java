package sport.app.sport_connecting_people.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private boolean isOAuth;
    private boolean isEnabled;
}