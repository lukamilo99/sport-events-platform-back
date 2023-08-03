package sport.app.sport_connecting_people.service;

import sport.app.sport_connecting_people.entity.User;

public interface PrincipalService {

    User getCurrentUser();

    Long getCurrentUserId();
}
