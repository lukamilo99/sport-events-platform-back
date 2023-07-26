package sport.app.sport_connecting_people.exceptions.jwt;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
