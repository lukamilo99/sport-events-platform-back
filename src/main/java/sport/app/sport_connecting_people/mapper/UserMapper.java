package sport.app.sport_connecting_people.mapper;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.entity.enums.AuthenticationProvider;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.security.model.OAuth2UserInformation;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@Component
public class UserMapper {

    public User mapToUser(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setProvider(AuthenticationProvider.local);
        return user;
    }

    public User updateUserData(User user, UserUpdateDto dto) {
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        return user;
    }

    public User mapToUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInformation oAuth2UserInfo) {
        User user = new User();
        String[] fullName = oAuth2UserInfo.getName().split(" ");
        String firstname = fullName[0];
        String lastname = fullName[1];

        user.setProvider(AuthenticationProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getProviderId());
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(oAuth2UserInfo.getEmail());
        return user;
    }

    public User updateOAuthUserData(User user, OAuth2UserInformation oAuth2UserInfo) {
        String[] fullName = oAuth2UserInfo.getName().split(" ");
        String firstname = fullName[0];
        String lastname = fullName[1];

        user.setFirstname(firstname);
        user.setLastname(lastname);
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
