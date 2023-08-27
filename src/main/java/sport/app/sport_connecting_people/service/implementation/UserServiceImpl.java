package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserProfileDto;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.specification.PrincipalService;
import sport.app.sport_connecting_people.service.specification.UserService;
import sport.app.sport_connecting_people.specification.UserSpecification;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PrincipalService principalService;
    private UserMapper userMapper;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Transactional
    @Override
    public void updateUser(UserUpdateDto dto) {
        User user = principalService.getCurrentUser();
        userRepository.save(userMapper.updateUserData(user, dto));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public void banUser(Long userId) {
        userRepository.updateUserEnabledStatus(userId, false);
    }

    @Transactional
    @Override
    public void unbanUser(Long userId) {
        userRepository.updateUserEnabledStatus(userId, true);
    }

    @Override
    public PaginatedUserResponseDto searchUsers(String name, Pageable pageable) {
        Specification<User> spec = Specification
                .where(UserSpecification.hasFirstName(name))
                .or(UserSpecification.hasLastName(name));
        Page<User> users = userRepository.findAll(spec, pageable);

        User currentUser = principalService.getCurrentUser();
        List<UserResponseDto> userDtos = users.getContent().stream().map(user -> {
            UserResponseDto dto = userMapper.mapToUserResponseDto(user);
            dto.setStatus(userRepository.findFriendshipRequestStatus(currentUser.getId(), user.getId()).orElse(RequestStatus.NOTHING));
            return dto;
        }).collect(Collectors.toList());

        PaginatedUserResponseDto response = new PaginatedUserResponseDto();
        response.setUsers(userDtos);
        response.setTotalCount(users.getTotalElements());
        return response;
    }

    @Override
    public PaginatedUserProfileDto searchUsersDetails(String name, Pageable pageable) {
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

    @Override
    public UserProfileDto getPrincipal(UserPrincipal userPrincipal) {
        return userMapper.mapToUserProfile(userPrincipal);
    }
}
