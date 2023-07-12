package sport.app.sport_connecting_people.security.model;

import java.util.Map;

public class GoogleOAuth2UserInformation extends OAuth2UserInformation{

    public GoogleOAuth2UserInformation(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
