package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;
import sport.app.sport_connecting_people.dto.user.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;

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
}
