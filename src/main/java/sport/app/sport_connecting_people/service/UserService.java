package sport.app.sport_connecting_people.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PrincipalService principalService;
    private UserMapper userMapper;

    public User update(UserUpdateDto dto) {
        User user = principalService.getCurrentUser();
        userRepository.save(userMapper.updateUser(user, dto));
        return user;
    }

    public void delete() {
        Long userId = principalService.getCurrentUserId();
        userRepository.deleteById(userId);
    }
}
