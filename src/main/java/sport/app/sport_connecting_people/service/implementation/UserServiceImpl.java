package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserProfileDto;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.UserService;
import sport.app.sport_connecting_people.specification.UserSpecification;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PrincipalServiceImpl principalServiceImpl;
    private UserMapper userMapper;

    @Transactional
    public void updateUser(UserUpdateDto dto) {
        User user = principalServiceImpl.getCurrentUser();
        userRepository.save(userMapper.updateUserData(user, dto));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void banUser(Long userId) {
        userRepository.updateUserEnabledStatus(userId, false);
    }

    @Transactional
    public void unbanUser(Long userId) {
        userRepository.updateUserEnabledStatus(userId, true);
    }

    public PaginatedUserProfileDto searchUsers(String name, Pageable pageable) {
        Specification<User> spec = Specification
                .where(UserSpecification.hasFirstName(name))
                .or(UserSpecification.hasLastName(name));

        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<UserProfileDto> eventDtos = userPage.getContent().stream()
                .map(event -> userMapper.mapToUserProfile(event))
                .toList();

        PaginatedUserProfileDto response = new PaginatedUserProfileDto();
        response.setUsers(eventDtos);
        response.setTotalCount(userPage.getTotalElements());

        return response;
    }

    public UserProfileDto getPrincipal(UserPrincipal userPrincipal) {
        return userMapper.mapToUserProfile(userPrincipal);
    }
}
