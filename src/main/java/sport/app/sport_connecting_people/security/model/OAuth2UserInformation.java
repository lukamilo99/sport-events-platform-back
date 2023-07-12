package sport.app.sport_connecting_people.security.model;

import java.util.Map;


public abstract class OAuth2UserInformation {

    protected Map<String, Object> attributes;

    public OAuth2UserInformation(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getProviderId();

    public abstract String getName();

    public abstract String getEmail();
}
