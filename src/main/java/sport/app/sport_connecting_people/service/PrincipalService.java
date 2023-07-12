package sport.app.sport_connecting_people.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.UserNotFoundException;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.util.SecurityUtil;

@AllArgsConstructor
@Service
public class PrincipalService {

    private UserRepository userRepository;

    public User getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserPrincipal().getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public Long getCurrentUserId() {
        return SecurityUtil.getCurrentUserPrincipal().getId();
    }
}
