package sport.app.sport_connecting_people.exceptions.authentication;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
