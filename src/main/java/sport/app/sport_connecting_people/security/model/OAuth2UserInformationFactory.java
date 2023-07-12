package sport.app.sport_connecting_people.security.model;

import sport.app.sport_connecting_people.entity.AuthenticationProvider;

import java.util.Map;

public class OAuth2UserInformationFactory {

    public static OAuth2UserInformation getOAuth2UserInformation(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthenticationProvider.google.toString())) {
            return new GoogleOAuth2UserInformation(attributes);
        }
        return null;
    }
}
