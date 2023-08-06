package sport.app.sport_connecting_people.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.authentication.CustomAuthenticationException;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.PrincipalService;

@AllArgsConstructor
@Service
public class PrincipalServiceImpl implements PrincipalService {

    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUserPrincipal().getId();
    }

    @Override
    public UserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        else {
            throw new CustomAuthenticationException("User not properly authenticated");
        }
    }

    @Override
    public void setCurrentUserPrincipal(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
