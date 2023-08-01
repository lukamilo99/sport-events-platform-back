package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PrincipalService principalService;
    private UserMapper userMapper;

    @Transactional
    public void update(UserUpdateDto dto) {
        User user = principalService.getCurrentUser();
        userRepository.save(userMapper.updateUserData(user, dto));
    }

    @Transactional
    public void delete() {
        Long userId = principalService.getCurrentUserId();
        userRepository.deleteById(userId);
    }

    public UserProfileDto me(UserPrincipal userPrincipal) {
        return userMapper.createUserProfile(userPrincipal);
    }
}
