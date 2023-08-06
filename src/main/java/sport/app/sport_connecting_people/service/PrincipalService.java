package sport.app.sport_connecting_people.service;

import org.springframework.security.core.Authentication;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

public interface PrincipalService {

    User getCurrentUser();

    Long getCurrentUserId();

    UserPrincipal getCurrentUserPrincipal();

    void setCurrentUserPrincipal(Authentication authentication);
}
