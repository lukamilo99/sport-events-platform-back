package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
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

    public User updateUserData(User user, UserUpdateDto dto) {
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getFirstname() + " " + user.getLastname());
        return dto;
    }

    public UserProfileDto mapToUserProfile(UserPrincipal userPrincipal) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(userPrincipal.getId());
        dto.setEmail(userPrincipal.getEmail());
        dto.setFirstname(userPrincipal.getFirstname());
        dto.setLastname(userPrincipal.getLastname());
        dto.setRole(userPrincipal.getAuthorities().toArray()[0].toString());
        dto.setOAuth(userPrincipal.isOAuth());
        dto.setEnabled(userPrincipal.isEnabled());
        return dto;
    }

    public UserProfileDto mapToUserProfile(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setRole(user.getRole().getName());
        dto.setOAuth(user.getProvider().toString().equals("google"));
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}
