package sport.app.sport_connecting_people.service;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserProfileDto;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

public interface UserService {

    void updateUser(UserUpdateDto dto);

    void deleteUser(Long userId);

    void banUser(Long userId);

    void unbanUser(Long userId);

    PaginatedUserProfileDto searchUsers(String name, Pageable pageable);

    UserProfileDto getPrincipal(UserPrincipal userPrincipal);
}
