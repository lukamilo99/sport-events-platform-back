package sport.app.sport_connecting_people.service.specification;

import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;


public interface AuthenticationService {

    void register(UserRegistrationDto dto);

    String login(UserLoginDto dto);
}
