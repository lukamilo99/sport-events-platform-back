package sport.app.sport_connecting_people.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.enums.AuthenticationProvider;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.authentication.OAuth2AuthenticationProcessingException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.model.OAuth2UserInformation;
import sport.app.sport_connecting_people.security.model.OAuth2UserInformationFactory;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@AllArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuthUser(userRequest, oAuth2User);
    }

    private OAuth2User processOAuthUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInformation oAuth2UserInfo = OAuth2UserInformationFactory
                .getOAuth2UserInformation(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationProcessingException("OAuth2 authorization failed");
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if(user != null) {
            if(!user.getProvider().equals(AuthenticationProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInformation oAuth2UserInfo) {
        User user = userMapper.mapToUser(oAuth2UserRequest, oAuth2UserInfo);
        user.setRole(roleRepository.findByName("USER"));
        userRepository.save(user);
        return user;
    }

    private void updateExistingUser(User user, OAuth2UserInformation oAuth2UserInfo) {
        userRepository.save(userMapper.updateOAuthUserData(user, oAuth2UserInfo));
    }
}
