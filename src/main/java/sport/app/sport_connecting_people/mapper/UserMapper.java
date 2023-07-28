package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.user.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;
import sport.app.sport_connecting_people.dto.user.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@AllArgsConstructor
@Component
public class UserMapper {

    private PasswordEncoder passwordEncoder;

    public User createUser(UserRegistrationDto dto) {
        return new User(dto.getEmail(),
                dto.getFirstname(),
                dto.getLastname(),
                passwordEncoder.encode(dto.getPassword()),
                "local");
    }

    public User updateUser(User user, UserUpdateDto dto) {
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserResponseDto createUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getFirstname() + " " + user.getLastname());
        return dto;
    }

    public UserProfileDto createUserProfile(UserPrincipal userPrincipal) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(userPrincipal.getId());
        dto.setEmail(userPrincipal.getEmail());
        dto.setFirstname(userPrincipal.getFirstname());
        dto.setLastname(userPrincipal.getLastname());
        dto.setRole(userPrincipal.getAuthorities().toArray()[0].toString());
        dto.setOAuth(userPrincipal.isOAuth());
        return dto;
    }
}
