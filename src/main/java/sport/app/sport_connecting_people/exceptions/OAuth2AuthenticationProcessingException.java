package sport.app.sport_connecting_people.exceptions;

public class OAuth2AuthenticationProcessingException extends RuntimeException {
    private String message;

    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }
}
